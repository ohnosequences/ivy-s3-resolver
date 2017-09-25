
```scala
package ohnosequences.ivy

import java.util.Date

import better.files._
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model._
import com.amazonaws.util.StringInputStream
import com.amazonaws.util.StringUtils.UTF8
import org.apache.ivy.plugins.repository.Resource
import org.scalamock.scalatest.MockFactory
import org.scalatest._

import scala.collection.convert.WrapAsJava._
import scala.util.Random

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 */
class S3RepositorySpec extends WordSpec with Matchers with Inside
  with MockFactory {

  "Resource factory" must {
    s"return ${classOf[S3Resource].getName} instance" which {
      "is cached with memoization" in {
        val bucketName = "bucketName"
        val objectKey = Random.nextString(5)
        val source = s"s3://$bucketName/$objectKey"

        val client = mock[AmazonS3]
```

Overlaps somewhat with tests for S3Resource itself because of a non-trivial constructor.

```scala
        (client.getObjectMetadata(_: String, _: String)).expects(bucketName, objectKey).once() returns {
          val metadata = mock[ObjectMetadata]
          (metadata.getContentLength _).expects().once().returns(0)
          (metadata.getLastModified _).expects().once().returns(new Date)
          metadata
        }

        val repository = new S3MockableRepository(client)

        inside(repository.getResource(source)) {
          case resource: S3Resource =>
            repository.getResource(source) should be theSameInstanceAs resource
        }
      }
    }
  }
```

TODO: Test transfer initiated signal.
TODO: Test progress listener.
Presently, only ensure the getResource method is called and bytes it provides are written to the destination, which produces a Resource instance that's tested independently with correct URI handling.

```scala
  "Get source" must {
    /** Work-around for paulbutcher/ScalaMock#114. */
    abstract class MockableResource extends Resource {
      override def clone(cloneName: String): Resource = this
    }

    s"use factory-provided ${classOf[Resource].getName}" in {
      val resource = mock[MockableResource]
      val repository = new S3MockableRepository(mock[AmazonS3]) {
        override def getResource(source: String) = resource
      }

      val content = Random.nextString(100)

      (resource.getName _).expects().once().returns(Random.nextString(10))
      (resource.getContentLength _).expects().atLeastOnce().returns(content.getBytes(UTF8).length)
      (resource.openStream _).expects().once().returns(new StringInputStream(content))

      val destination = File.newTemporaryFile()
      repository.get("", destination.toJava)

      destination.contentAsString should be(content)
    }
  }
```

Verifies listing normalizes the object key, and marshals results from the client correctly.
TODO: Verify correct iteration over subsequent markers.

```scala
  "List parent" when {
    Scenarios.locationWithBucketNameAndObjectKeys foreach {
      case (given, (bucketName, objectKey)) =>
        s"""given "$given"""" must {
          "list objects" in {
            val client = mock[AmazonS3]

            val requestPredicate = { request: ListObjectsRequest =>
              bucketName == request.getBucketName &&
                objectKey == request.getPrefix
            }

            val directories =
              objectKey + "/ivys" ::
                objectKey + "/jars" ::
                Nil

            val files =
              for {
                directory <- directories
                file <- "fileA" :: "fileB" :: Nil
              } yield directory + "/" + file

            (client.listObjects(_: ListObjectsRequest)).expects(where(requestPredicate)).once() returns {
              val listing = mock[ObjectListing]
              (listing.getCommonPrefixes _).expects().once().returns(directories)
              (listing.getObjectSummaries _).expects().once() returns {
                files map { file =>
                  val summary = mock[S3ObjectSummary]
                  (summary.getKey _).expects().once().returns(file)
                  summary
                }
              }
              (listing.getNextMarker _).expects().once().returns(null)
              listing
            }

            val repository = new S3MockableRepository(client)
            repository.list(given) should contain theSameElementsAs directories ++ files
          }
        }
    }
  }
```

TODO: Test storage class.
TODO: Test ACL assignment.
TODO: Test encryption.
TODO: Test overwrite.
TODO: Test bucket creation.

```scala
  "Put source to destination" when {
    val content = Random.nextString(100)
    val source = File.newTemporaryFile().overwrite(content)

    Scenarios.locationWithBucketNameAndObjectKeys foreach {
      case (given, (bucketName, objectKey)) =>
        s"""given "$given"""" must {
          "put objects" in {
            val client = mock[AmazonS3]

            val requestPredicate = { request: PutObjectRequest =>
              bucketName == request.getBucketName &&
                objectKey == request.getKey
            }

            (client.doesBucketExistV2 _).expects(bucketName).once().returns(true)
            (client.putObject(_: PutObjectRequest)).expects(where(requestPredicate))

            val repository = new S3MockableRepository(client)
            repository.put(source.toJava, given, true)
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