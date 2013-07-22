import sbtrelease._

import ReleaseStateTransformations._

import ivyS3ResolverBuild._

name := "ivy-s3-resolver"

organization := "ohnosequences"

version := "0.0.5"

scalaVersion := "2.10.2"

publishMavenStyle := true

publishTo <<= version { (v: String) =>
  if (v.trim.endsWith("SNAPSHOT"))
    Some(Resolver.file("local-snapshots", file("artifacts/snapshots.ohnosequences.com")))
  else
    Some(Resolver.file("local-releases", file("artifacts/releases.ohnosequences.com")))
}

resolvers ++= Seq (
                    "Typesafe Releases"   at "http://repo.typesafe.com/typesafe/releases",
                    "Sonatype Releases"   at "https://oss.sonatype.org/content/repositories/releases",
                    "Sonatype Snapshots"  at "https://oss.sonatype.org/content/repositories/snapshots",
                    "Era7 Releases"       at "http://releases.ohnosequences.com.s3.amazonaws.com",
                    "Era7 Snapshots"      at "http://snapshots.ohnosequences.com.s3.amazonaws.com"
                  )


libraryDependencies += "com.amazonaws" % "aws-java-sdk" % "1.5.1"

libraryDependencies += "org.apache.ivy" % "ivy" % "2.3.0"



