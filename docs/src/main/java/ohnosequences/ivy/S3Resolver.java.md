
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

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import org.apache.ivy.plugins.resolver.RepositoryResolver;
import com.amazonaws.services.s3.model.Region;

/**
 * A dependency resolver that looks to an S3 repository to resolve dependencies.
 *
 * @author Ben Hale
 * @author Evdokim Kovach
 */
public class S3Resolver extends RepositoryResolver {

	public S3Resolver(String name, String accessKey, String secretKey, boolean overwrite, Region region) {
		setName(name);
		setRepository(new S3Repository(accessKey, secretKey, overwrite, region));
	}

	public S3Resolver(String name, String accessKey, String secretKey, boolean overwrite, Region region, CannedAccessControlList acl, boolean serverSideEncryption) {
		setName(name);
		setRepository(new S3Repository(accessKey, secretKey, overwrite, region, acl,serverSideEncryption));
	}

	public S3Resolver(String name, AWSCredentialsProvider credentialsProvider, boolean overwrite, Region region) {
		setName(name);
		setRepository(new S3Repository(credentialsProvider, overwrite, region));
	}

	public S3Resolver(String name, AWSCredentialsProvider credentialsProvider, boolean overwrite, Region region, CannedAccessControlList acl, boolean serverSideEncryption) {
		setName(name);
		setRepository(new S3Repository(credentialsProvider, overwrite, region, acl,serverSideEncryption));
	}

	public String getTypeName() {
		return "s3";
	}
}

```




[main/java/ohnosequences/ivy/S3Repository.java]: S3Repository.java.md
[main/java/ohnosequences/ivy/S3RepositoryException.java]: S3RepositoryException.java.md
[main/java/ohnosequences/ivy/S3Resolver.java]: S3Resolver.java.md
[main/java/ohnosequences/ivy/S3Resource.java]: S3Resource.java.md
[main/java/ohnosequences/ivy/S3Utils.java]: S3Utils.java.md