package dev.sachin

import cats.effect.{ ExitCode, IO, Resource, ResourceApp }
import dev.sachin.config.ApplicationConfig
import dev.sachin.db.TransactorModule
import dev.sachin.db.TransactorModule.DBTransactor
import dev.sachin.kafka.Fs2ProducerSettings
import dev.sachin.service.{ ColumnParser, FileReader, PublishData }
import fs2.kafka.KafkaProducer
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.nio.file.{ Path => JPath }

object Main extends ResourceApp {

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): Resource[IO, ExitCode] = {
    for {
      _               <- Resource.eval(Logger[IO].info("Acquiring resources...."))
      applicationConf <- ApplicationConfig.resource
      kafkaProducer   <- KafkaProducer.resource(Fs2ProducerSettings.apply(applicationConf))
      transactor = TransactorModule.dataSource(applicationConf.database)
      _ <- Resource.eval(program(kafkaProducer, transactor))
    } yield ExitCode.Success
  }

  private def program(kafkaProducer: KafkaProducer[IO, String, Array[Byte]], transactor: DBTransactor): IO[Unit] = {
    for {
      _ <- Logger[IO].info("Loading Program....")
      nseColumnParser = ColumnParser.nseParser
      dataStream      = FileReader.impl.readFile(JPath.of("./src/main/resources/HDFC_A.tsv"))(nseColumnParser)
      _ <- PublishData.impl(kafkaProducer, dataStream).run()
      _ <- dataStream.compile.drain
    } yield ()
  }

}
