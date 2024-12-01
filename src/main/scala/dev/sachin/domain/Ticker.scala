package dev.sachin.domain

import cats.effect.{Deferred, ExitCode, IO, IOApp}
import play.api.libs.json.{Format, Json, OFormat}

import scala.concurrent.duration.DurationInt

case class Ticker(symbol: String) extends AnyVal

object Ticker {

  implicit val format: Format[Ticker] = Json.valueFormat[Ticker]
//  sealed trait Type
//
//  object Type {
//    case object Stock  extends Type
//    case object Index  extends Type
//    case object Sector extends Type
//  }
//
//  sealed trait Market
//  object Market {
//    case object Indian extends Market
//  }

}

object Test extends IOApp {
  import cats.effect.{IO, Deferred}
  import cats.syntax.all._

  private def start(d: Deferred[IO, Unit]) = {
    IO.sleep(2.second) *> IO.println("stream starting") *> d.complete(())
  }.start


  def run(args: List[String]): IO[ExitCode] =
    for {
      d <- Deferred[IO, Unit]
      _ <- start(d)
      _ <- IO.println("happening parallel to stream start")
      _ <- d.get
      _ <- IO.println("stream started")
    } yield ExitCode.Success
}
