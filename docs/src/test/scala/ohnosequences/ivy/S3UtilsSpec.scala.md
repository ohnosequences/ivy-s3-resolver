
```scala
package ohnosequences.ivy

import org.scalatest._

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 */
class S3UtilsSpec extends WordSpec with Matchers {

  "Getting buckets and keys" when {
    Scenarios.locationWithBucketNameAndObjectKeys foreach {
      case (given, (bucketName, objectKey)) =>
        s"""given "$given"""" must {
          s"""return bucket name "$bucketName"""" in {
            S3Utils.getBucket(given) should equal(bucketName)
          }
          s"""return object key "$objectKey"""" in {
            S3Utils.getKey(given) should equal(objectKey)
          }
        }
    }

  }

}

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