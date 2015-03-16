Nice.javaProject

name := "ivy-s3-resolver"
organization := "ohnosequences"

bucketSuffix := "era7.com"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-s3" % "1.9.24",
  "org.apache.ivy" % "ivy" % "2.4.0"
)
