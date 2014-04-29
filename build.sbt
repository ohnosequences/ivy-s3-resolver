Nice.javaProject

name := "ivy-s3-resolver"

organization := "ohnosequences"

publishMavenStyle := true

bucketSuffix := "era7.com"

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk" % "1.7.7",
  "org.apache.ivy" % "ivy" % "2.3.0"
)

// javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

// otherwise there are problems with building documentation
// publishArtifact in packageDoc := false
