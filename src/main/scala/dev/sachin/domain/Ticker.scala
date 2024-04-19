package dev.sachin.domain

case class Ticker(symbol: String, tickerType: Ticker.Type)

object Ticker {
  sealed trait Type

  object Type {
    case object Stock  extends Type
    case object Index  extends Type
    case object Sector extends Type
  }

  sealed trait Market
  object Market {
    case object Indian extends Market
  }

}
