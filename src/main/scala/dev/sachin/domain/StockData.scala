package dev.sachin.domain

import play.api.libs.json.{Json, OFormat}

import java.time.Instant

sealed trait StockData

object StockData {
  // todo use java money later if needed
  case class NSEData(
      date: Instant,
      series: String,
      open: BigDecimal,
      high: BigDecimal,
      low: BigDecimal,
      prevClose: BigDecimal,
      // ignore LTP
      close: BigDecimal,
      vwap: BigDecimal,
      `52WeekHigh`: BigDecimal,
      `52WeekLow`: BigDecimal,
      volume: Long,
      value: BigDecimal, // total amount of trade in a day
      noOfTrades: Long
  ) extends StockData

  object NSEData {
    implicit val format: OFormat[NSEData] = Json.format
  }
}
