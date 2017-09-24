package ohnosequences.ivy

/**
 * @author <a href="michael@ahlers.consulting">Michael Ahlers</a>
 */
object Scenarios {

  val locationWithBucketNameAndObjectKeys: List[(String, (String, String))] =
    ("s3://bucket/path0", ("bucket", "path0")) ::
      ("s3://bucket//path0", ("bucket", "path0")) ::
      ("s3://bucket/path0/path1", ("bucket", "path0/path1")) ::
      ("s3://bucket/path0//path1", ("bucket", "path0/path1")) ::
      ("s3://bucket//path0/path1", ("bucket", "path0/path1")) ::
      ("s3://bucket//path0//path1", ("bucket", "path0/path1")) ::
      Nil

}
