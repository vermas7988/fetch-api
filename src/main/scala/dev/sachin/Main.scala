package dev.sachin

import cats.effect.{ ExitCode, IO, IOApp }
import dev.sachin.service.{ ColumnParser, FileReader }
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

import java.nio.file.{ Path => JPath }

object Main extends IOApp {

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]

  override def run(args: List[String]): IO[ExitCode] = {
    for {
      _ <- Logger[IO].info("Here starts the program")
      nseColumnParser = ColumnParser.nseParser
      _ <- FileReader.impl.readFile(JPath.of("./src/main/resources/HDFC_A.tsv"))(nseColumnParser)
    } yield ExitCode.Success
  }

}
