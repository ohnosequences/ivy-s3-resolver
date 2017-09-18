enablePlugins(JavaOnlySettings)

name := "ivy-s3-resolver"
organization := "ohnosequences"

bucketSuffix := "era7.com"

javaVersion := "1.7"
javacOptions ++= Seq(
  "-Xlint:deprecation"
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.197",
  "org.apache.ivy" % "ivy" % "2.4.0"
)

bintrayReleaseOnPublish := true
bintrayOrganization := Some(organization.value)
bintrayPackageLabels := Seq("ivy", "s3", "resolver")

publishTo := (publishTo in bintray).value
