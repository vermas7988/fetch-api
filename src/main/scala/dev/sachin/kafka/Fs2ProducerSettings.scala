package dev.sachin.kafka

import cats.effect.IO
import dev.sachin.config.ApplicationConfig
import fs2.kafka._

object Fs2ProducerSettings {
  def apply(applicationConf: ApplicationConfig): ProducerSettings[IO, String, Array[Byte]] =
    ProducerSettings[IO, String, Array[Byte]]
      .withBootstrapServers(applicationConf.kafka.bootstrapServer)
      .withRetries(Int.MaxValue)

}
