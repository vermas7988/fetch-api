package dev.sachin

import cats.effect.{ExitCode, IO, Resource, ResourceApp}
import dev.sachin.service.StockProcessor
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.nio.file.Files

object Main extends ResourceApp {

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): Resource[IO, ExitCode] = {
    for {
      _ <- Resource.eval(Logger[IO].info("Acquiring resources...."))
//      applicationConf <- ApplicationConfig.resource
//      kafkaProducer   <- KafkaProducer.resource(Fs2ProducerSettings.apply(applicationConf))
//      transactor = TransactorModule.dataSource(applicationConf.database)
      _ <- Resource.eval(program())
    } yield ExitCode.Success
  }

  private def program(): IO[Unit] = {
    for {
      _ <- Logger[IO].info("Loading Program....")
      _ <- Logger[IO].info("Loading Program....")
      resourceStream = getClass.getResourceAsStream("/data/nse/stocks/HDFC_temp.csv")
      path <- IO {
        val tempFile = Files.createTempFile("HDFC_temp", ".csv")
        Files.copy(resourceStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        tempFile
      }
      _ <- new StockProcessor().computeDailyLogReturn(path)
//      _ <- Logger[IO].info(s"Lines: $lines")
    } yield ()
  }

}
