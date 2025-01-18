package dev.sachin.kafka

import cats.effect.IO
import dev.sachin.config.ApplicationConfig
import fs2.kafka.{ConsumerSettings, KeyDeserializer, ValueDeserializer}

object Fs2KafkaConsumerSettings {

  def apply[K, V](applicationConf: ApplicationConfig)(implicit
      keyDeserializer: KeyDeserializer[IO, K],
      valueDeserializer: ValueDeserializer[IO, V]
  ): ConsumerSettings[IO, K, V] = {
    ConsumerSettings[IO, K, V].withBootstrapServers(applicationConf.kafka.bootstrapServer)
  }

}
