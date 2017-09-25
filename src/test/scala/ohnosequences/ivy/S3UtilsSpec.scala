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
