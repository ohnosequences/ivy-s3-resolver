Nice.javaProject

name := "ivy-s3-resolver"
organization := "ohnosequences"

bucketSuffix := "era7.com"

javaVersion := "1.7"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-s3" % "1.10.47",
  "org.apache.ivy" % "ivy" % "2.4.0"
)
