bintrayOrganization := Some(organization.value)
bintrayPackageLabels := Seq("ivy", "s3", "resolver")

publishTo := (publishTo in bintray).value
