package dev.sachin.config

import ApplicationConfig._
import cats.effect.{ IO, Resource }
import pureconfig._
import pureconfig.generic.auto._

final case class ApplicationConfig private (kafka: KafkaConfig, database: DbConfig)

object ApplicationConfig {
  final case class KafkaConfig private (bootstrapServer: String)

  final case class DbConfig(
      connectionThreads: Int,
      driver: String,
      url: String,
      user: String,
      pass: String
  )

  def resource: Resource[IO, ApplicationConfig] =
    Resource.eval(IO.blocking(ConfigSource.default.at("app").loadOrThrow[ApplicationConfig]))
}
