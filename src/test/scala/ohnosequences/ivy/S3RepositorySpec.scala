package ohnosequences.ivy

import java.util.Date

import better.files._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.{CannedAccessControlList, ObjectMetadata, StorageClass}
import com.amazonaws.util.StringInputStream
import com.amazonaws.util.StringUtils.UTF8
import org.apache.ivy.plugins.repository.Resource
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.util.Random

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 */
class S3RepositorySpec extends WordSpec with Matchers with Inside
  with MockFactory {

  /** Work-around for paulbutcher/ScalaMock#114. */
  abstract class TestResource extends Resource {
    override def clone(cloneName: String): Resource = ???
  }

  /** More concise construction. */
  class TestS3Repository(
    s3Client: AmazonS3 = mock[AmazonS3],
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

  "Resource factory" must {
    s"return ${classOf[S3Resource].getName} instance" which {
      "is cached with memoization" in {
        val bucketName = Random.nextString(5)
        val objectKey = Stream.fill(2)(Random.nextString(5)).mkString("/")
        val source = s"s3://$bucketName/$objectKey"

        val client = mock[AmazonS3]

        (client.getObjectMetadata(_: String, _: String)).expects(*, objectKey).once() returns {
          val metadata = mock[ObjectMetadata]
          (metadata.getContentLength _).expects().once().returns(0)
          (metadata.getLastModified _).expects().once().returns(new Date)
          metadata
        }

        val repository = new TestS3Repository(client)

        inside(repository.getResource(source)) {
          case resource: S3Resource =>
            repository.getResource(source) should be theSameInstanceAs resource
        }
      }
    }
  }

  /*
   * TODO: Test transfer initiated signal and progress listener.
   * As of this writing, only ensure the getResource method is called and bytes it provides are written to the destination, which produces a Resource instance that's tested independently with correct URI handling.
   */
  "Get source" must {
    s"use factory-provided ${classOf[Resource].getName}" in {
      val resource = mock[TestResource]
      val repository = new TestS3Repository {
        override def getResource(source: String) = resource
      }

      val content = new StringInputStream(Random.nextString(100))

      (resource.getName _).expects().once().returns(Random.nextString(10))
      (resource.getContentLength _).expects().atLeastOnce().returns(content.getString.getBytes(UTF8).length)
      (resource.openStream _).expects().once().returns(content)

      val destination = File.newTemporaryFile()
      repository.get("", destination.toJava)

      destination.contentAsString should be(content.getString)
    }
  }

}
