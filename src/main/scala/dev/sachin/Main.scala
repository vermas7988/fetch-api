package dev.sachin

import cats.effect.{ExitCode, IO, IOApp}
import org.typelevel.log4cats.Logger
import org.typelevel.log4cats.slf4j.Slf4jLogger

object Main extends IOApp {

  implicit val logger: Logger[IO] = Slf4jLogger.getLogger[IO]


  override def run(args: List[String]): IO[ExitCode] = {
    for{
      _ <- Logger[IO].info("Here starts the program")
    } yield ExitCode.Success
  }


}
