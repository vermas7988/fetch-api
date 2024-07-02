package dev.sachin.db

import cats.effect.IO
import dev.sachin.config.ApplicationConfig.DbConfig
import doobie.util.transactor.Transactor

object TransactorModule {

  type DBTransactor = Transactor.Aux[IO, Unit]

  def dataSource(config: DbConfig): Transactor.Aux[IO, Unit] = Transactor.fromDriverManager[IO](
    driver = config.driver,
    url = config.url,
    user = config.user,
    password = config.pass,
    logHandler = None
  )

}
