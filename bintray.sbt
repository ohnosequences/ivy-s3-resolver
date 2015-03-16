import bintray.Keys._

bintrayPublishSettings

bintrayOrganization in bintray := Some(organization.value)

packageLabels in bintray := Seq("ivy", "s3", "resolver")

repository in bintray := "maven"
