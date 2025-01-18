package dev.sachin.domain

import play.api.libs.json.{Format, Json}

case class Ticker(symbol: String) extends AnyVal

object Ticker {

  implicit val format: Format[Ticker] = Json.valueFormat[Ticker]
//  sealed trait Market
//  object Market {
//    case object Indian extends Market
//  }

}
