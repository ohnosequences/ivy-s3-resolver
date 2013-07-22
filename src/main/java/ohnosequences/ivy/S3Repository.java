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
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import org.apache.ivy.plugins.repository.AbstractRepository;
import org.apache.ivy.plugins.repository.RepositoryCopyProgressListener;
import org.apache.ivy.plugins.repository.Resource;
import org.apache.ivy.plugins.repository.TransferEvent;
import org.apache.ivy.util.FileUtil;

/**
 * A repository the allows you to upload and download from an S3 repository.
 * 
 * @author Ben Hale
 * @author Evdokim Kovach
 */
public class S3Repository extends AbstractRepository {

	private String accessKey;

	private String secretKey;

    private AmazonS3Client s3Client;

	private Map<String, S3Resource> resourceCache = new HashMap<String, S3Resource>();

	public void setAccessKey(String accessKey) {
		this.accessKey = accessKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public void get(String source, File destination) {
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
		if (!resourceCache.containsKey(source)) {
			resourceCache.put(source, new S3Resource(getClient(), source));
		}
		return resourceCache.get(source);
	}

    @Override
	public List<String> list(String parent) {


		String bucket = S3Utils.getBucket(parent);
		String key = S3Utils.getKey(parent);

		try {
            List<S3ObjectSummary> summaries = getClient().listObjects(bucket, key).getObjectSummaries();

			List<String> keys = new ArrayList<String>(summaries.size());
			for (S3ObjectSummary summary : summaries) {
				keys.add(summary.getKey());
			}
			return keys;
		}
		catch (AmazonServiceException e) {
			throw new S3RepositoryException(e);
		}
	}

	@Override
	protected void put(File source, String destination, boolean overwrite) {
		String bucket = S3Utils.getBucket(destination);
		String key = S3Utils.getKey(destination);
        System.out.println("fake publish: bucket=" + bucket + " key=" + key);
        PutObjectRequest request = new PutObjectRequest(bucket , key, source);
        request = request.withCannedAcl(CannedAccessControlList.PublicRead);

        getClient().putObject(request);


	}

	private AmazonS3Client getClient() {
		if (s3Client == null) {
			try {
                s3Client = new AmazonS3Client(new BasicAWSCredentials(accessKey, secretKey));
                s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1));
			}
			catch (AmazonServiceException e) {
				throw new S3RepositoryException(e);
			}
		}
		return s3Client;
	}


}
