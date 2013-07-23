import sbtrelease._

import ReleaseStateTransformations._

import ivyS3ResolverBuild._

name := "ivy-s3-resolver"

organization := "ohnosequences"

version := "0.0.6"

scalaVersion := "2.9.2"

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  if (v.trim.endsWith("SNAPSHOT"))
    Some(Resolver.file("local-snapshots", file("artifacts/snapshots.era7.com")))
  else
    Some(Resolver.file("local-releases", file("artifacts/releases.era7.com")))
}

resolvers ++= Seq (
                    "Typesafe Releases"   at "http://repo.typesafe.com/typesafe/releases",
                    "Sonatype Releases"   at "https://oss.sonatype.org/content/repositories/releases",
                    "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots",
                    "Era7 Releases"       at "http://releases.era7.com.s3.amazonaws.com",
                    "Era7 Snapshots"      at "http://snapshots.era7.com.s3.amazonaws.com"
                  )


libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.5.1"

libraryDependencies += "org.apache.ivy" % "ivy" % "2.3.0"



