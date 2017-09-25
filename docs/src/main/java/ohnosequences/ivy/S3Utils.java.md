
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

import java.net.URI;
import java.net.URISyntaxException;


/**
 * A utility class for parsing full URIs into S3 component parts.
 *
 * @author Ben Hale
 * @author Evdokim Kovach
 */
public class S3Utils {

  /**
   * Parses the S3 bucket from a properly formed S3 URI.
   * @param uri The URI to parse
   * @return An S3 bucket for the given URI
   */
  public static String getBucket(String uri) {
    String bucket = getUri(uri).getHost();
    return bucket;
  }

  /**
   * Parses the S3 key name from a properly formed S3URI.
   * @param uri The URI to parse
   * @return An S3 key name for the given URI
   */
  public static String getKey(String uri) {
    String key = getUri(uri).getPath().substring(1);
    return key;
  }

  private static URI getUri(String uri) {
    try {
```

For ohnosequences/sbt-s3-resolver#52, has the effect of cleaning up redundant path delimiters (thereby fixing invalid S3 object keys).

```java
      return new URI(uri).normalize();
    } catch (URISyntaxException e) {
      throw new IllegalArgumentException("'" + uri + "' is a malformed S3 URI");
    }
  }

}

```




[main/java/ohnosequences/ivy/S3Repository.java]: S3Repository.java.md
[main/java/ohnosequences/ivy/S3RepositoryException.java]: S3RepositoryException.java.md
[main/java/ohnosequences/ivy/S3Resolver.java]: S3Resolver.java.md
[main/java/ohnosequences/ivy/S3Resource.java]: S3Resource.java.md
[main/java/ohnosequences/ivy/S3Utils.java]: S3Utils.java.md
[test/scala/ohnosequences/ivy/S3MockableRepository.scala]: ../../../../test/scala/ohnosequences/ivy/S3MockableRepository.scala.md
[test/scala/ohnosequences/ivy/S3RepositorySpec.scala]: ../../../../test/scala/ohnosequences/ivy/S3RepositorySpec.scala.md
[test/scala/ohnosequences/ivy/S3ResourceSpec.scala]: ../../../../test/scala/ohnosequences/ivy/S3ResourceSpec.scala.md
[test/scala/ohnosequences/ivy/S3UtilsSpec.scala]: ../../../../test/scala/ohnosequences/ivy/S3UtilsSpec.scala.md
[test/scala/ohnosequences/ivy/Scenarios.scala]: ../../../../test/scala/ohnosequences/ivy/Scenarios.scala.md