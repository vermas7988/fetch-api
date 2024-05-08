package dev.sachin.service

import cats.effect.IO
import dev.sachin.config.KafkaConnections
import dev.sachin.kafka.Fs2KafkaConsumerSettings
import dev.sachin.service.ProcessData.consumerSettings
import fs2.kafka.{ AutoOffsetReset, IsolationLevel, KafkaConsumer }

class ProcessData {

  def run(): fs2.Stream[IO, Unit] = {

    KafkaConsumer.resource(consumerSettings).evalTap(_.subscribeTo(KafkaConnections.tickerData))
    ???
  }

}

object ProcessData {

  val consumerSettings = Fs2KafkaConsumerSettings.kafkaConsumerSettings
    .withAutoOffsetReset(AutoOffsetReset.Earliest)
    .withIsolationLevel(IsolationLevel.ReadCommitted)
    .withEnableAutoCommit(false)
}
