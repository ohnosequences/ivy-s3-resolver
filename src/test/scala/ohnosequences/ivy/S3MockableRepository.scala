package ohnosequences.ivy

import java.util.Optional
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
  acl: Optional[CannedAccessControlList] = Optional.of(CannedAccessControlList.PublicRead),
  serverSideEncryption: Boolean = false,
  storageClass: StorageClass = StorageClass.Standard
) extends S3Repository(
  s3Client,
  overwrite,
  acl,
  serverSideEncryption,
  storageClass
)
