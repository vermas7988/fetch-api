package dev.sachin.domain

import play.api.libs.json.{Json, OFormat}

import java.time.Instant

sealed trait TickerData {
  def date: Instant
  def open: BigDecimal
  def close: BigDecimal

}

object TickerData {
  // todo use java money later if needed
  case class StockData(
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
  ) extends TickerData

  object StockData {
    implicit val format: OFormat[StockData] = Json.format
  }

  case class BankNiftyData(
      date: Instant,
      expiryDate: Instant,
      optionType: String,
      strikePrice: BigDecimal,
      open: BigDecimal,
      highPrice: BigDecimal,
      lowPrice: BigDecimal,
      close: BigDecimal,
      lastPrice: BigDecimal,
      settlePrice: BigDecimal,
      volume: Long,
      value: BigDecimal,
      premiumValue: BigDecimal,
      openInterest: Long,
      changeInOI: Long
  ) extends TickerData

  object BankNiftyData {
    implicit val format: OFormat[BankNiftyData] = Json.format
  }

}
