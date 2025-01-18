package dev.sachin.service

import cats.effect.IO
import com.github.tototoshi.csv.*
import dev.sachin.domain.StockData.NSEData
import fs2.Stream

import java.nio.file.Path as JPath
import java.text.SimpleDateFormat

class StockProcessor() {

  def rankVWAPwithHighestVolume(filepath: JPath): IO[Unit] = {
    for {
      lines <- readFileAndParseColumns(filepath)
      _ <- IO {
        val sorted = lines.sortBy(_.volume).reverse

        sorted.take(5).foreach(println)
      }
    } yield ()
  }

  def computeDailyLogReturn(filepath: JPath): IO[Unit] = {
    for {
      lines <- readFileAndParseColumns(filepath)
      _ <- IO {
        val sorted = lines.sortBy(_.date.toEpochMilli)

        sorted.sliding(2).foreach {
          case List(prev, curr) =>
            val logReturn = Math.log(curr.close.toDouble / prev.close.toDouble)
            println(s"${curr.date} : $logReturn")

          case _ => ()
        }
      }
    } yield ()
  }

  def readFileAndParseColumns(filepath: JPath): IO[List[NSEData]] = {
    readFile(filepath)
      .drop(1)
      .map(parse)
      .compile
      .toList
  }

  private def readFile(filepath: JPath): Stream[IO, Array[String]] = {
    val lines = CSVReader
      .open(filepath.toFile)
      .iterator
      .map(_.toArray.map(_.trim.strip().replace(",", "")))
      .toList

    Stream.emits(lines)
  }

  private def parse(cols: Array[String]): NSEData = {
    cols match {
      case Array(
            date,
            series,
            open,
            high,
            low,
            prevClose,
            _,
            close,
            vwap,
            fiftyTwoWeekHigh,
            fiftyTwoWeekLow,
            volume,
            value,
            noOfTrades
          ) =>
        NSEData(
          date = new SimpleDateFormat("dd-MMM-yyyy").parse(date).toInstant,
          series = series,
          open = BigDecimal(open),
          high = BigDecimal(high),
          low = BigDecimal(low),
          prevClose = BigDecimal(prevClose),
          close = BigDecimal(close),
          vwap = BigDecimal(vwap),
          `52WeekHigh` = BigDecimal(fiftyTwoWeekHigh),
          `52WeekLow` = BigDecimal(fiftyTwoWeekLow),
          volume = volume.toLong,
          value = BigDecimal(value),
          noOfTrades = noOfTrades.toLong
        )

      case x =>
        x.foreach(println)
        throw new RuntimeException("No implementation found")
    }
  }

}
