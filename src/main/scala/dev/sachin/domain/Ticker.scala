package dev.sachin.domain

import play.api.libs.json.{ Format, Json, OFormat }

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
