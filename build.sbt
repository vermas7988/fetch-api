import Dependencies.*

ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "2.13.13"
ThisBuild / version := "0.0.1"
ThisBuild / organization := "dev.sachin"
ThisBuild / organizationName := "Sachin"

ThisBuild / evictionErrorLevel := Level.Warn

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .enablePlugins(AshScriptPlugin) // generate binary using Ash shell for Docker compatibility
  .settings(
    name := "fetch-api",
    Docker / packageName := "fetch-api",
    dockerUpdateLatest := true,
    dockerExposedPorts := Seq(8080),
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    makeBatScripts := Seq(), // don't generate windows binary on publish
    scalacOptions ++= List("-Ymacro-annotations", "-Yrangepos", "-Wconf:cat=unused:info"),
    scalafmtOnCompile := true,
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    libraryDependencies ++= Seq(
      CompilerPlugins.betterMonadicFor,
      CompilerPlugins.kindProjector,
      Libraries.cats,
      Libraries.catsEffect,
      Libraries.catsRetry,
      Libraries.doobieCore,
      Libraries.doobieDB,
      Libraries.fs2Core,
      Libraries.fs2IO,
      Libraries.fs2Kafka,
      Libraries.log4cats,
      Libraries.logback % Runtime,
      Libraries.pureConfig,
      Libraries.playJson
    )
  )
  .settings(flywaySettings)
  .enablePlugins(FlywayPlugin)

lazy val flywaySettings = Seq(
  flywayUrl := "jdbc:postgresql://localhost:5432/fetch_api;shutdown=true",
  flywayUser := "fetch_api_user",
  flywayPassword := "fetch_api_user",
  flywayLocations := Seq("db/migration"),
  flywayDriver := "org.postgresql.Driver"
//  Test/ flywayUrl  := "jdbc:hsqldb:file:target/flyway_sample;shutdown=true",
//  Test/ flywayUser := "SA",
//  Test/ flywayLocations := Seq("filesystem:src/main/resources/db/migration"),
//  Test/ flywayPassword := "",
)

addCommandAlias("fmt", ";tpolecatCiMode;scalafmtSbt;scalafmtAll")
addCommandAlias("fmtCheck", ";tpolecatCiMode;scalafmtCheckAll")
addCommandAlias("c", "compile")
