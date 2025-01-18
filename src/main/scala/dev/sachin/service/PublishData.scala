package dev.sachin.service

import cats.effect.IO
import dev.sachin.config.KafkaConnections
import dev.sachin.domain.TickerData.StockData
import fs2.Stream
import fs2.kafka.{KafkaProducer, ProducerRecord, ProducerRecords}
import play.api.libs.json.Json

import java.nio.charset.StandardCharsets

sealed trait PublishData[T] {

  val producerKafkaTopic: String = KafkaConnections.tickerData // todo configure

  def run(): IO[Unit]
}

object PublishData {

  def impl(
      kafkaProducer: KafkaProducer[IO, String, Array[Byte]],
      dataStream: Stream[IO, StockData]
  ): PublishData[StockData] =
    new PublishData[StockData] {
      override def run(): IO[Unit] =
        dataStream
          .map { nseDataRow =>
            val json = Json.toJson(nseDataRow)
            Json.stringify(json)
          }
          .map { jsonStr =>
            val producerRecord = ProducerRecord(
              producerKafkaTopic,
              "hdfc",
              jsonStr.getBytes(StandardCharsets.UTF_8)
            ) // todo use proper key
            ProducerRecords.one(producerRecord)
          }
          .evalMap(kafkaProducer.produce)
//        .void // todo check if needed
          .compile
          .drain
    }
}
