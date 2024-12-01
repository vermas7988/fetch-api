package dev.sachin.service

import cats.effect.IO
import cats.implicits._
import dev.sachin.domain.StockData.NSEData
import fs2._
import fs2.io.file.{ Files, Path }

import java.nio.file.{ Path => JPath }

trait FileReader[T, F[_]] {
  def readFile(filepath: JPath)(implicit columnParser: ColumnParser[T]): Stream[F, T]
}

object FileReader {
  // todo add unit tests and find solution for removing first line of file by program
  def impl: FileReader[NSEData, IO] =
    new FileReader[NSEData, IO] {
      override def readFile(filepath: JPath)(implicit columnParser: ColumnParser[NSEData]): Stream[IO, NSEData] =
        Files[IO]
          .readAll(Path.fromNioPath(filepath))
          .through(text.utf8.decode)
          .through(text.lines)
          .map(_.replace(",", ""))
          .map(_.split("\t"))
          .map(columnParser.parse)
    }
}
