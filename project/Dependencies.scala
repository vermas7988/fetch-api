import sbt.*

object Dependencies {


  object Versions {

    val cats          = "2.10.0"
    val catsEffect    = "3.5.4"
    val catsRetry     = "3.1.3"

    val http4s        = "0.23.26"

    val log4cats      = "2.6.0"
    val logback          = "1.5.6"

    val betterMonadicFor = "0.3.1"
    val kindProjector    = "0.13.3"
  }

  object Libraries {

    def http4s(artifact: String): ModuleID = "org.http4s" %% s"http4s-$artifact" % Versions.http4s

    val cats       = "org.typelevel"    %% "cats-core"   % Versions.cats
    val catsEffect = "org.typelevel"    %% "cats-effect" % Versions.catsEffect
    val catsRetry  = "com.github.cb372" %% "cats-retry"  % Versions.catsRetry

    val log4cats = "org.typelevel" %% "log4cats-slf4j" % Versions.log4cats
    val logback = "ch.qos.logback" % "logback-classic" % Versions.logback


    // Test
    val log4catsNoOp      = "org.typelevel"       %% "log4cats-noop"      % Versions.log4cats


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
