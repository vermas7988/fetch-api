import Dependencies.*

ThisBuild / scalaVersion := "3.6.2"
ThisBuild / version := "0.0.1"
ThisBuild / organization := "dev.sachin"
ThisBuild / organizationName := "Sachin"
ThisBuild / evictionErrorLevel := Level.Warn

resolvers ++= Resolver.sonatypeOssRepos("snapshots")

lazy val root = (project in file("."))
  .enablePlugins(DockerPlugin, JavaAppPackaging)
  .enablePlugins(AshScriptPlugin)
  .settings(
    name := "fetch-api",
    Docker / packageName := "fetch-api",
    dockerUpdateLatest := true,
    dockerExposedPorts := Seq(8080),
    dockerBaseImage := "openjdk:11-jre-slim-buster",
    makeBatScripts := Seq(),
    scalacOptions := scalacOptions.value.filterNot(_ == "-Ykind-projector") ++ List(
      "-source:future",
      "-Xkind-projector"
    ),
    scalafmtOnCompile := true,
    resolvers ++= Resolver.sonatypeOssRepos("snapshots"),
    libraryDependencies ++= Seq(
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
      Libraries.playJson,
      Libraries.csv
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
)

addCommandAlias("fmt", ";tpolecatCiMode;scalafmtSbt;scalafmtAll")
addCommandAlias("fmtCheck", ";tpolecatCiMode;scalafmtCheckAll")
addCommandAlias("c", "compile")
