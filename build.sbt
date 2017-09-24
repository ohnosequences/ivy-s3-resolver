enablePlugins(JavaOnlySettings)

name := "ivy-s3-resolver"
organization := "ohnosequences"

bucketSuffix := "era7.com"

javaVersion := "1.8"
javacOptions ++= Seq(
  "-Xlint:deprecation"
)

libraryDependencies ++= Seq(
  "com.amazonaws" % "aws-java-sdk-s3" % "1.11.197",
  "org.apache.ivy" % "ivy" % "2.4.0"
)

/* Be more permissive for test code. */
wartremoverErrors in(Test, compile) --= Seq(Wart.Any, Wart.AsInstanceOf, Wart.DefaultArguments, Wart.NonUnitStatements, Wart.Null)

libraryDependencies ++= Seq(
  "com.github.pathikrit" %% "better-files" % "2.17.1" % Test,
  "org.scalamock" %% "scalamock-scalatest-support" % "3.6.0" % Test,
  "org.scalatest" %% "scalatest" % "3.0.1" % Test
)

licenses := Seq("Apache-2.0" -> url("https://www.apache.org/licenses/LICENSE-2.0"))

bintrayReleaseOnPublish := true
bintrayOrganization := Some(organization.value)
bintrayPackageLabels := Seq("ivy", "s3", "resolver")

publishTo := (publishTo in bintray).value
