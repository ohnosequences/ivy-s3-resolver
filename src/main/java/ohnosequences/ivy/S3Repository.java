/*
 * Copyright 2004-2007 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package ohnosequences.ivy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.AmazonClientException;
import com.amazonaws.auth.*;
import com.amazonaws.internal.*;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import org.apache.ivy.plugins.repository.AbstractRepository;
import org.apache.ivy.plugins.repository.RepositoryCopyProgressListener;
import org.apache.ivy.plugins.repository.Resource;
import org.apache.ivy.plugins.repository.TransferEvent;
import org.apache.ivy.util.FileUtil;
import org.apache.ivy.util.Message;



/**
 * A repository the allows you to upload and download from an S3 repository.
 *
 * @author Ben Hale
 * @author Evdokim Kovach
 */
public class S3Repository extends AbstractRepository {

	private AmazonS3Client s3Client;

	private Map<String, S3Resource> resourceCache = new HashMap<String, S3Resource>();

	private Region region;

	private boolean overwrite;

	private CannedAccessControlList acl;

	public S3Repository(String accessKey, String secretKey, boolean overwrite, Region region) {
		this(accessKey, secretKey, overwrite, region, CannedAccessControlList.Private);
	}

	public S3Repository(String accessKey, String secretKey, boolean overwrite, Region region, CannedAccessControlList acl) {
		AWSCredentialsProvider provider = new InstanceProfileCredentialsProvider();
		try {
			provider.getCredentials();
		} catch (AmazonClientException e1) {
			provider = new StaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
		}

		s3Client = new AmazonS3Client(provider);
		this.overwrite = overwrite;
		this.region = region;
		this.acl = acl;
	}

	public S3Repository(AWSCredentialsProvider provider, boolean overwrite, Region region) {
		this(provider, overwrite, region, CannedAccessControlList.Private);
	}

	public S3Repository(AWSCredentialsProvider provider, boolean overwrite, Region region, CannedAccessControlList acl) {
		s3Client = new AmazonS3Client(provider);
		this.overwrite = overwrite;
		this.region = region;
		this.acl = acl;
	}

	public void get(String source, File destination) {
		//System.out.println("get source=" + source + " dst=" + destination.getPath());
		Resource resource = getResource(source);
		try {
			fireTransferInitiated(resource, TransferEvent.REQUEST_GET);
			RepositoryCopyProgressListener progressListener = new RepositoryCopyProgressListener(this);
			progressListener.setTotalLength(resource.getContentLength());
			FileUtil.copy(resource.openStream(), new FileOutputStream(destination), progressListener);
		}
		catch (IOException e) {
			fireTransferError(e);
			throw new Error(e);
		}
		catch (RuntimeException e) {
			fireTransferError(e);
			throw e;
		}
		finally {
			fireTransferCompleted(resource.getContentLength());
		}
	}

	public Resource getResource(String source) {
		// if (!source.startsWith("s3:")) {
		// 	return new org.apache.ivy.plugins.repository.BasicResource("", false, 0, 0, false);
		// }
		//System.out.println("getResource> " + source);
		if (!resourceCache.containsKey(source)) {
			resourceCache.put(source, new S3Resource(this, source));
		}
		return resourceCache.get(source);
	}

	@Override
	public List<String> list(String parent) {
		try {
			String marker = null;
			List<String> keys = new ArrayList<String>();

			do {
				ListObjectsRequest request = new ListObjectsRequest()
						.withBucketName(S3Utils.getBucket(parent))
						.withPrefix(S3Utils.getKey(parent))
						.withDelimiter("/") // RFC 2396
						.withMarker(marker);

				ObjectListing listing = getS3Client().listObjects(request);

				// Add "directories"
				keys.addAll(listing.getCommonPrefixes());

				// Add "files"
				for (S3ObjectSummary summary : listing.getObjectSummaries()) {
					keys.add(summary.getKey());
				}

				marker = listing.getNextMarker();
			} while (marker != null);

			return keys;
		}
		catch (AmazonServiceException e) {
			throw new S3RepositoryException(e);
		}
	}

	private boolean createBucket(String name, Region region) {
		int attemptLimit = 5;
		int timeout = 1000 * 20;
		int attempt = 0;

		while(attempt < attemptLimit) {
			try {
				attempt++;

				getS3Client().createBucket(name, region);
				if(getS3Client().doesBucketExist(name)) {
					return true;
				}

			} catch(AmazonS3Exception s3e) {
				try {
					Message.warn(s3e.toString());
					Thread.sleep(timeout);
				} catch (InterruptedException e) {
				}
			}
		}
		return false;
	}

	@Override
	protected void put(File source, String destination, boolean overwrite) {
		//System.out.print("parent> ");
		String bucket = S3Utils.getBucket(destination);
		String key = S3Utils.getKey(destination);
		// System.out.println("publishing: bucket=" + bucket + " key=" + key);
		PutObjectRequest request = new PutObjectRequest(bucket ,key, source);
		request = request.withCannedAcl(acl);

		if (!getS3Client().doesBucketExist(bucket)) {
			if(!createBucket(bucket, region)) {
				throw new Error("couldn't create bucket");
			}
		}

		if (!this.overwrite && !getS3Client().listObjects(bucket, key).getObjectSummaries().isEmpty()) {
			throw new Error(destination + " exists but overwriting is disabled");
		}
		getS3Client().putObject(request);

	}

	public AmazonS3Client getS3Client() {
		return s3Client;
	}

}
