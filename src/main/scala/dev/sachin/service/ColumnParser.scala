package dev.sachin.service

import dev.sachin.domain.StockData.NSEData

import java.text.SimpleDateFormat

trait ColumnParser[T] {

  def parse(cols: Array[String]): T

}

object ColumnParser {
  // fixme make partial func orElse add error handling
  def nseParser: ColumnParser[NSEData] = {
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
