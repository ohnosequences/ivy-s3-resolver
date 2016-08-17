## ivy-s3-resolver

[![](https://travis-ci.org/ohnosequences/ivy-s3-resolver.svg?branch=master)](https://travis-ci.org/ohnosequences/ivy-s3-resolver)
[![](https://img.shields.io/codacy/93e453cd1c3f44d2862b9b751da9d0c9.svg)](https://www.codacy.com/app/ohnosequences/ivy-s3-resolver)
[![](http://github-release-version.herokuapp.com/github/ohnosequences/ivy-s3-resolver/release.svg)](https://github.com/ohnosequences/ivy-s3-resolver/releases/latest)
[![](https://img.shields.io/badge/license-AGPLv3-blue.svg)](https://tldrlegal.com/license/gnu-affero-general-public-license-v3-%28agpl-3.0%29)
[![](https://img.shields.io/badge/contact-gitter_chat-dd1054.svg)](https://gitter.im/ohnosequences/ivy-s3-resolver)

Ivy resolver that supports publishing to S3 objects.

To add a dependency in your sbt project use this:

```scala
resolvers += "Era7 maven releases" at "http://releases.era7.com.s3.amazonaws.com"

libraryDependencies += "ohnosequences" % "ivy-s3-resolver" % "<version>"
```

Normally you won't use this resolver directly, see [sbt-s3-resolver](https://github.com/ohnosequences/sbt-s3-resolver) instead.


See also [API documentation](http://ohnosequences.com/ivy-s3-resolver/docs/api/latest/).
