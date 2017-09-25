* #19: Normalize object key when getting resources (by Michael Ahlers @michaelahlers)
  + This is an important fix that eliminates redundant delimiters in the S3 object paths. See [ohnosequences/sbt-s3-resolver#52](https://github.com/ohnosequences/sbt-s3-resolver/issues/52) and [sbt/sbt#3573](https://github.com/sbt/sbt/issues/3573) for the context.
  + This pull-request also adds a whole new testing infrastructure (non-existent before) and tests for the key methods of the resolver.
* Updated aws-java-sdk-s3 from v1.11.197 to v1.11.202
