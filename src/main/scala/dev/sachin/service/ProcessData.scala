package dev.sachin.service

import cats.effect.IO
import cats.effect.kernel.Resource
import dev.sachin.config.{ApplicationConfig, KafkaConnections}
import dev.sachin.domain.TickerData
import dev.sachin.domain.TickerData.StockData
import dev.sachin.kafka.Fs2KafkaConsumerSettings
import fs2.Chunk
import fs2.kafka.{
  AutoOffsetReset,
  CommittableConsumerRecord,
  CommittableOffsetBatch,
  ConsumerSettings,
  IsolationLevel,
  KafkaConsumer
}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger
import play.api.libs.json.Json

import scala.concurrent.duration.DurationInt

trait ProcessData {
  def run: Resource[IO, fs2.Stream[IO, Unit]]
}

object ProcessData {

  def impl(applicationConfig: ApplicationConfig): ProcessData = new ProcessData {

    implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

    val consumerSettings: ConsumerSettings[IO, String, Array[Byte]] =
      Fs2KafkaConsumerSettings[String, Array[Byte]](applicationConfig)
        .withAutoOffsetReset(AutoOffsetReset.Earliest)
        .withIsolationLevel(IsolationLevel.ReadCommitted)
        .withEnableAutoCommit(false)

    override def run: Resource[IO, fs2.Stream[IO, Unit]] = {
      KafkaConsumer
        .resource(consumerSettings)
        .evalTap(_.subscribeTo(KafkaConnections.tickerData))
        .map(_.records)
        .map(_.through(process()))
    }

    def process(): fs2.Pipe[IO, CommittableConsumerRecord[IO, String, Array[Byte]], Unit] =
      _.evalTap(logIncomingRecords)
        .groupWithin(50, 10.seconds)
        .evalMap { chunk =>
          val batch = CommittableOffsetBatch.fromFoldableMap(chunk)(_.offset)
          (??? : IO[Unit]) >> batch.commit
        }

    def processChunk(chunk: Chunk[CommittableConsumerRecord[IO, String, Array[Byte]]]): IO[Unit] = {

      ???
    }

    def logIncomingRecords(record: CommittableConsumerRecord[IO, String, Array[Byte]]) = {
      Logger[IO].info(s"Process stream received record with key: ${record.record.key}")
    }

    def deserialize(record: Array[Byte]) = {
      Json.parse(record).as[StockData] // todo make generic for all markets
    }

  }

}
