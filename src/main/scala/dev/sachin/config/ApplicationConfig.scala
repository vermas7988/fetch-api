package dev.sachin.config

import cats.effect.{IO, Resource}
import dev.sachin.config.ApplicationConfig.*
import pureconfig.*
import pureconfig.ConfigReader.Result

final case class ApplicationConfig private (kafka: KafkaConfig, database: DbConfig) derives ConfigReader

object ApplicationConfig {
  final case class KafkaConfig private (bootstrapServer: String)

  final case class DbConfig(
      connectionThreads: Int,
      driver: String,
      url: String,
      user: String,
      pass: String
  ) derives ConfigReader

  def resource: Resource[IO, ApplicationConfig] =
    Resource.eval(IO.blocking(ConfigSource.default.at("app").loadOrThrow[ApplicationConfig]))
}
