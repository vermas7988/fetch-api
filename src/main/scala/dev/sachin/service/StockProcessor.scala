package dev.sachin.service

import cats.effect.IO
import com.github.tototoshi.csv.*
import dev.sachin.Main.getClass
import dev.sachin.domain.TickerData
import dev.sachin.domain.TickerData.StockData
import fs2.Stream

import java.nio.file.{Files, Path as JPath}
import java.text.SimpleDateFormat

class StockProcessor() {
  // BANKNIFTY-19-11-2024-to-19-01-2025.csv

  
  def calculate55DaysMovingAverage(filepath: JPath): IO[Unit] = {
    for {
      lines <- readFileAndParseColumns(filepath)
      _ <- IO {
        val sorted = lines.sortBy(_.date.toEpochMilli)

        sorted.sliding(55).foreach { window =>
          val movingAverage = window.map(_.close.toDouble).sum / 55
          println(s"${window.last.date} : $movingAverage")
        }
      }
    } yield ()
  }

  def calculateVolumeToPriceChangeRatioForStock(filepath: JPath): IO[Unit] = {
    for {
      lines <- readFileAndParseColumns(filepath).map(_.asInstanceOf[List[StockData]])
      _ <- IO {
        val sorted = lines.sortBy(_.date.toEpochMilli)

        sorted.sliding(2).foreach {
          case List(prev, curr) =>
            val volumeToPriceChangeRatio = formulaForPercentageChangeInVolumeToPercentPriceChangeRatio(prev, curr)
            println(s"${curr.date} : $volumeToPriceChangeRatio")

          case _ => ()
        }
      }
    } yield ()
  }

  def formulaForPercentageChangeInVolumeToPercentPriceChangeRatio(prev: StockData, curr: StockData): Double = {
    val volumeChange = curr.volume - prev.volume
    val priceChange = curr.close.toDouble - prev.close.toDouble
    val volumeChangePercentage = volumeChange / prev.volume
    val priceChangePercentage = priceChange / prev.close.toDouble
    volumeChangePercentage / priceChangePercentage
  }



  def printStockAndBankNiftyLogReturns(): IO[Unit] = {
    for {
//      _ <- computeDailyLogReturnsForStock()
//      _ <- IO(println("Bank Nifty"))
//      _ <- computeDailyLogReturnsForBankNifty()
      _ <- IO(println("Volume to Price Change Ratio"))
      resourceStream = getClass.getResourceAsStream("/data/nse/stocks/HDFC_temp.csv")
      path <- IO {
        val tempFile = Files.createTempFile("HDFC_temp", ".csv")
        Files.copy(resourceStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        tempFile
      }
      _ <- calculateVolumeToPriceChangeRatioForStock(path)
    } yield ()
  }

  def computeDailyLogReturnsForStock(): IO[Unit] = {
    for {
      _ <- IO.unit
      resourceStream = getClass.getResourceAsStream("/data/nse/stocks/HDFC_temp.csv")
      path <- IO {
        val tempFile = Files.createTempFile("HDFC_temp", ".csv")
        Files.copy(resourceStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        tempFile
      }
      _ <- computeDailyLogReturn(path)
    } yield ()
  }

  def computeDailyLogReturnsForBankNifty(): IO[Unit] = {
    for {
      _ <- IO.unit
      resourceStream = getClass.getResourceAsStream("/data/nse/stocks/BANKNIFTY-19-11-2024-to-19-01-2025.csv")
      path <- IO {
        val tempFile = Files.createTempFile("BANKNIFTY-19-11-2024-to-19-01-2025", ".csv")
        Files.copy(resourceStream, tempFile, java.nio.file.StandardCopyOption.REPLACE_EXISTING)
        tempFile
      }
      _ <- computeDailyLogReturn(path)
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

  def readFileAndParseColumns(filepath: JPath): IO[List[TickerData]] = {
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

  private def parse(cols: Array[String]): TickerData = {
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
        StockData(
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

      case Array(
            date,
            expiryDate,
            optionType,
            _,
            openPrice,
            highPrice,
            lowPrice,
            closePrice,
            lastPrice,
            settlePrice,
            volume,
            value,
            premiumValue,
            openInterest,
            changeInOI
          ) =>
        TickerData.BankNiftyData(
          date = new SimpleDateFormat("dd-MMM-yyyy").parse(date).toInstant,
          expiryDate = new SimpleDateFormat("dd-MMM-yyyy").parse(expiryDate).toInstant,
          optionType = optionType,
          strikePrice = BigDecimal(0),
          open = BigDecimal(openPrice),
          highPrice = BigDecimal(highPrice),
          lowPrice = BigDecimal(lowPrice),
          close = BigDecimal(closePrice),
          lastPrice = BigDecimal(lastPrice),
          settlePrice = BigDecimal(settlePrice),
          volume = volume.toLong,
          value = BigDecimal(value),
          premiumValue = BigDecimal(premiumValue),
          openInterest = openInterest.toLong,
          changeInOI = changeInOI.toLong
        )

      case x =>
        x.foreach(println)
        throw new RuntimeException("No implementation found")
    }
  }

}
