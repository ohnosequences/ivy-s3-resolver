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

              /* Verifies correct object key normalization. */
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
