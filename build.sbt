import sbtrelease._

name := "ivy-s3-resolver"

organization := "ohnosequences"


scalaVersion := "2.10.2"

crossScalaVersions := Seq("2.9.2", "2.10.2")


publishMavenStyle := true

//// For publishing set s3credentialsFile
publishTo <<= (isSnapshot, s3credentials) { 
                (snapshot,   credentials) => 
  val prefix = if (snapshot) "snapshots" else "releases"
  credentials map S3Resolver(
      "Era7 "+prefix+" S3 bucket"
    , "s3://"+prefix+".era7.com"
    // , Resolver.ivyStylePatterns
    ).toSbtResolver
}

resolvers ++= Seq (
  "Typesafe Releases"   at "http://repo.typesafe.com/typesafe/releases"
, "Sonatype Releases"   at "https://oss.sonatype.org/content/repositories/releases"
, "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots"
, "Era7 Releases"       at "http://releases.era7.com.s3.amazonaws.com"
, "Era7 Snapshots"      at "http://snapshots.era7.com.s3.amazonaws.com"
)

libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.6.1"

libraryDependencies += "org.apache.ivy" % "ivy" % "2.3.0"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")

// otherwise there are problems with building documentation
publishArtifact in packageDoc := false


releaseSettings
