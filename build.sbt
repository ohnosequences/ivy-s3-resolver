import sbtrelease._

import ReleaseStateTransformations._

import ivyS3ResolverBuild._

name := "ivy-s3-resolver"

organization := "ohnosequences"

scalaVersion := "2.9.2"

publishMavenStyle := true

s3credentialsFile in Global := Some("/home/evdokim/era7.prop")


publishTo <<= (isSnapshot, s3credentials) {
                (snapshot,   credentials) =>
  val prefix = if (snapshot) "snapshots" else "releases"
  credentials map s3resolver("Era7 "+prefix+" S3 bucket", "s3://"+prefix+".era7.com")
}

resolvers ++= Seq (
                    "Typesafe Releases"   at "http://repo.typesafe.com/typesafe/releases",
                    "Sonatype Releases"   at "https://oss.sonatype.org/content/repositories/releases",
                    "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots",
                    "Era7 Releases"       at "http://releases.era7.com.s3.amazonaws.com",
                    "Era7 Snapshots"      at "http://snapshots.era7.com.s3.amazonaws.com"
                  )


libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.5.5"

libraryDependencies += "org.apache.ivy" % "ivy" % "2.3.0"

javacOptions ++= Seq("-source", "1.6", "-target", "1.6")



