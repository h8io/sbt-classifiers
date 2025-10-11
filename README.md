# sbt-dependencies

SBT dependencies groups helper.

⚠️ SBT 1.8.0 or newer required.

## Usage

### plugins.sbt

```scala
addSbtPlugin("io.h8.sbt" %% "sbt-dependencies" % "x.x.x")
```

[![GitHub release](https://img.shields.io/github/v/release/h8io/sbt-dependencies)](https://github.com/h8io/sbt-dependencies/releases/latest)

### build.sbt

Import the package `h8io.sbt.dependencies.*`

Add group dependencies:

```scala
libraryDependencies ++= (
  Seq(
    "com.fasterxml.jackson.core" % "jackson-databind",
    "com.fasterxml.jackson.core" % "jackson-core",
    "com.fasterxml.jackson.module" %% "jackson-module-scala"
  ) % "2.14.1") ++ (
  "org.apache.spark" %% Seq(
    "spark-core",
    "spark-sql",
    "spark-streaming"
  ) % "3.3.1") ++ (
  "org.junit.jupiter" % Seq(
    "junit-jupiter-api",
    "junit-jupiter-engine",
    "junit-jupiter-params"
  ) % "5.9.1" % Test)
```

Other modules modifiers (like `exclude`, `excludeAll` or `force`) are applicable too.
