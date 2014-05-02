
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


```java
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

    private S3Repository s3Repo;

	private String bucket;

	private String key;

	private boolean exists;

	private long contentLength;

	private long lastModified;

	private String name;



	public S3Resource(S3Repository s3Repo, String uri) {
		this.s3Repo = s3Repo;
		initializeS3(uri);
		initalizeResource();
		this.name = uri;
	}

	public Resource clone(String newUri) {
		return new S3Resource(s3Repo, newUri);
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
            return s3Repo.getS3Client().getObject(bucket, key).getObjectContent();
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
			// System.out.println("trying to resolve bucket=" + bucket + " key=" + key);
			ObjectMetadata metadata = s3Repo.getS3Client().getObjectMetadata(bucket, key);

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

```


------

### Index

+ src
  + main
    + java
      + ohnosequences
        + ivy
          + [S3Repository.java][main/java/ohnosequences/ivy/S3Repository.java]
          + [S3RepositoryException.java][main/java/ohnosequences/ivy/S3RepositoryException.java]
          + [S3Resolver.java][main/java/ohnosequences/ivy/S3Resolver.java]
          + [S3Resource.java][main/java/ohnosequences/ivy/S3Resource.java]
          + [S3Utils.java][main/java/ohnosequences/ivy/S3Utils.java]

[main/java/ohnosequences/ivy/S3Repository.java]: S3Repository.java.md
[main/java/ohnosequences/ivy/S3RepositoryException.java]: S3RepositoryException.java.md
[main/java/ohnosequences/ivy/S3Resolver.java]: S3Resolver.java.md
[main/java/ohnosequences/ivy/S3Resource.java]: S3Resource.java.md
[main/java/ohnosequences/ivy/S3Utils.java]: S3Utils.java.md