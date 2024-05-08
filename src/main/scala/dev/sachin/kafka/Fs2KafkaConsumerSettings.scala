package dev.sachin.kafka

import cats.effect.IO
import fs2.kafka.ConsumerSettings

object Fs2KafkaConsumerSettings {

  def kafkaConsumerSettings[K, V]: ConsumerSettings[IO, K, V] = ???

}
