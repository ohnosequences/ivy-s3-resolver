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

import java.io.InputStream;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import org.apache.ivy.plugins.repository.Resource;


/**
 * A Resource implementation that extracts its data from an S3 resource.
 * 
 * @author Ben Hale
 * @author Evdokim Kovach
 */
public class S3Resource implements Resource {

    private AmazonS3Client s3Client;

	private String bucket;

	private String key;

	private boolean exists;

	private long contentLength;

	private long lastModified;

	private String name;



	public S3Resource(AmazonS3Client client, String uri) {
		this.s3Client = client;
		initializeS3(uri);
		initalizeResource();
		this.name = uri;
	}

	public Resource clone(String newUri) {
		return new S3Resource(s3Client, newUri);
	}

	public boolean exists() {
		return exists;
	}

	public long getContentLength() {
		return contentLength;
	}

	public long getLastModified() {
		return lastModified;
	}

	public String getName() {
		return name;
	}

	public boolean isLocal() {
		return false;
	}

	public InputStream openStream() {
		try {
            return s3Client.getObject(bucket, key).getObjectContent();
		}
		catch (AmazonServiceException e) {
			throw new S3RepositoryException(e);
		}
	}

	private void initializeS3(String uri) {
		this.bucket = S3Utils.getBucket(uri);
		this.key = S3Utils.getKey(uri);
	}

	private void initalizeResource() {
		try {
			System.out.println("trying to resolve bucket=" + bucket + " key=" + key);
            ObjectMetadata metadata = s3Client.getObjectMetadata(bucket, key);

			this.exists = true;
			this.contentLength = metadata.getContentLength();
			this.lastModified = metadata.getLastModified().getTime();
			
		}
		catch (AmazonServiceException e) {
			this.exists = false;
			this.contentLength = 0;
			this.lastModified = 0;
			this.name = "";
		}
	}

}
