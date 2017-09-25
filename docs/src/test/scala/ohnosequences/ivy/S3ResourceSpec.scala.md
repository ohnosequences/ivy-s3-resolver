
```scala
package ohnosequences.ivy

import java.util.Date

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.ObjectMetadata
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.util.Random

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 */
class S3ResourceSpec extends WordSpec with Matchers
  with MockFactory
  with PrivateMethodTester {

  "Constructor" when {
    Scenarios.locationWithBucketNameAndObjectKeys foreach {
      case (given, (bucketName, objectKey)) =>
        s"""given source "$given"""" must {
          "initialize properties correctly" in {
            val repository = mock[S3MockableRepository]

            val contentLength = Random.nextInt
            val lastModified = Random.nextLong

            (repository.getS3Client _).expects().once() returns {
              val client = mock[AmazonS3]
```

Verifies correct object key normalization.

```scala
              (client.getObjectMetadata(_: String, _: String)).expects(bucketName, objectKey).once() returns {
                val metadata = mock[ObjectMetadata]
                (metadata.getContentLength _).expects().once().returns(contentLength)
                (metadata.getLastModified _).expects().once().returns(new Date(lastModified))
                metadata
              }
              client
            }

            val resource = new S3Resource(repository, given)

            resource should have(
              'exists (true),
              'getContentLength (contentLength),
              'getLastModified (lastModified),
              'getName (given)
            )
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