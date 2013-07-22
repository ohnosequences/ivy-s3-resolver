package ohnosequences

import java.io.File
import com.amazonaws.services.s3.AmazonS3Client
import com.amazonaws.auth.PropertiesCredentials
import com.amazonaws.regions.Region
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.model.{CannedAccessControlList, PutObjectRequest}

object S3Sync {

  def sync(path: File, bucket: String) {
    val files= recursiveListFiles(path).filterNot(_.isDirectory)
    val s3Client = new AmazonS3Client(new PropertiesCredentials(new File("AwsCredentials.properties")))
    s3Client.setRegion(Region.getRegion(Regions.EU_WEST_1))


    if (!s3Client.doesBucketExist(bucket)) {
      s3Client.createBucket(bucket)
    }

    files.foreach { file =>
      val prefix = path.getPath + File.separator
      val key = file.getPath.replace(prefix, "")
      println(">>uploading " + key)
      s3Client.putObject(new PutObjectRequest(bucket , key, file)
        .withCannedAcl(CannedAccessControlList.PublicRead)
      )
    }

  }

  def recursiveListFiles(file: File, exclude: List[File] = Nil, root: Boolean = true): List[File] = root match {
    case false =>
      if (exclude.contains(file)) {
        Nil
      } else {
        val these = file.listFiles.toList
        these ++ these.filter(_.isDirectory).flatMap(recursiveListFiles(_, exclude, false))
      }
    case true => file +: recursiveListFiles(file, exclude, false)
  }
}
