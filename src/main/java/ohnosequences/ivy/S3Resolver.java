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

import org.apache.ivy.plugins.resolver.RepositoryResolver;

/**
 * A dependency resolver that looks to an S3 repository to resolve dependencies.
 * 
 * @author Ben Hale
 * @author Evdokim Kovach
 */
public class S3Resolver extends RepositoryResolver {
	
	public void setAccessKey(String accessKey) {
		((S3Repository)getRepository()).setAccessKey(accessKey);
	}
	
	public void setSecretKey(String secretKey) {
		((S3Repository)getRepository()).setSecretKey(secretKey);
	}

	public S3Resolver() {
		setRepository(new S3Repository());
	}

	public String getTypeName() {
		return "S3";
	}
}
