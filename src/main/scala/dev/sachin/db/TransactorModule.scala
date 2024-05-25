package dev.sachin.db

import cats.effect.IO
import doobie.util.transactor.Transactor

object TransactorModule {

  def dataSource() = {
    Transactor.fromDriverManager[IO](
      driver = "org.postgresql.Driver",
      url = "jdbc:postgresql:world",
      user = "postgres",
      password = "password",
      logHandler = None)
  }

}
