
```scala
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