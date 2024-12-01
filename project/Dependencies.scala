import sbt.*

object Dependencies {

  object Versions {

    val cats       = "2.10.0"
    val catsEffect = "3.5.4"
    val catsRetry  = "3.1.3"
    val doobie     = "1.0.0-RC4"
    val fs2        = "3.10.2"
    val fs2Kafka   = "3.5.0"

    val http4s = "0.23.26"

    val log4cats   = "2.6.0"
    val logback    = "1.5.6"
    val pureConfig = "0.17.6"
    val playJson   = "3.0.2"

    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.3"
  }

  object Libraries {

    private def http4s(artifact: String): ModuleID = "org.http4s"   %% s"http4s-$artifact" % Versions.http4s
    private def doobie(artifact: String): ModuleID = "org.tpolecat" %% s"doobie-$artifact" % Versions.doobie
    private def fs2(artifact: String): ModuleID    = "co.fs2"       %% s"fs2-$artifact"    % Versions.fs2

    val cats       = "org.typelevel" %% "cats-core" % Versions.cats
    val catsEffect = "org.typelevel" %% "cats-effect" % Versions.catsEffect
    val catsRetry  = "com.github.cb372" %% "cats-retry" % Versions.catsRetry
    val fs2Core    = fs2("core")
    val fs2IO      = fs2("io")
    val fs2Kafka   = "com.github.fd4s" %% "fs2-kafka" % Versions.fs2Kafka

    val doobieCore = doobie("core")
    val doobieDB   = doobie("postgres")

    val log4cats   = "org.typelevel"         %% "log4cats-slf4j" % Versions.log4cats
    val logback    = "ch.qos.logback"        % "logback-classic" % Versions.logback
    val pureConfig = "com.github.pureconfig" %% "pureconfig"     % Versions.pureConfig
    val playJson   = "org.playframework"     %% "play-json"      % Versions.playJson

    // Test
    val log4catsNoOp = "org.typelevel" %% "log4cats-noop" % Versions.log4cats

  }

  object CompilerPlugins {
    val betterMonadicFor = compilerPlugin(
      "com.olegpy" %% "better-monadic-for" % Versions.betterMonadicFor
    )
    val kindProjector = compilerPlugin(
      "org.typelevel" % "kind-projector" % Versions.kindProjector cross CrossVersion.full
    )
  }

}
