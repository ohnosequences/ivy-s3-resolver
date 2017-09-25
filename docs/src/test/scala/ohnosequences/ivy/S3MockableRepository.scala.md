
```scala
package ohnosequences.ivy

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{CannedAccessControlList, StorageClass}

/**
 * Allows more concise construction by tests.
 *
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 */
class S3MockableRepository(
  s3Client: AmazonS3,
  overwrite: Boolean = true,
  acl: CannedAccessControlList = CannedAccessControlList.PublicRead,
  serverSideEncryption: Boolean = false,
  storageClass: StorageClass = StorageClass.Standard
) extends S3Repository(
  s3Client,
  overwrite,
  acl,
  serverSideEncryption,
  storageClass
)

```




[main/java/ohnosequences/ivy/S3Repository.java]: ../../../../main/java/ohnosequences/ivy/S3Repository.java.md
[main/java/ohnosequences/ivy/S3RepositoryException.java]: ../../../../main/java/ohnosequences/ivy/S3RepositoryException.java.md
[main/java/ohnosequences/ivy/S3Resolver.java]: ../../../../main/java/ohnosequences/ivy/S3Resolver.java.md
[main/java/ohnosequences/ivy/S3Resource.java]: ../../../../main/java/ohnosequences/ivy/S3Resource.java.md
[main/java/ohnosequences/ivy/S3Utils.java]: ../../../../main/java/ohnosequences/ivy/S3Utils.java.md
[test/scala/ohnosequences/ivy/S3MockableRepository.scala]: S3MockableRepository.scala.md
[test/scala/ohnosequences/ivy/S3RepositorySpec.scala]: S3RepositorySpec.scala.md
[test/scala/ohnosequences/ivy/S3ResourceSpec.scala]: S3ResourceSpec.scala.md
[test/scala/ohnosequences/ivy/S3UtilsSpec.scala]: S3UtilsSpec.scala.md
[test/scala/ohnosequences/ivy/Scenarios.scala]: Scenarios.scala.md