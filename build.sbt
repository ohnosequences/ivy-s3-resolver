enablePlugins(JavaOnlySettings)

name := "ivy-s3-resolver"
organization := "ohnosequences"

bucketSuffix := "era7.com"

javaVersion := "1.7"

javacOptions ++= Seq(
  "-Xlint:deprecation"
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.147",
  "org.apache.ivy" % "ivy" % "2.4.0"
)
