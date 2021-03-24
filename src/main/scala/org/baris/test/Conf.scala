package org.baris.test

import io.github.gaelrenoux.tranzactio.ErrorStrategies
import zio.duration.durationInt
import zio.{Has, Layer, ZLayer}

object Conf {

  type Conf = Has[Conf.Root]

  case class Root(db: DbConf,
                  dbRecovery: ErrorStrategies,
                  alternateDbRecovery: ErrorStrategies)

  case class DbConf(url: String, username: String, password: String)

  def live(dbName: String): Layer[Nothing, Has[Root]] = ZLayer.succeed(
    Conf.Root(
      db = DbConf(s"jdbc:postgresql:$dbName", "postgres", ""),
      dbRecovery = ErrorStrategies
        .timeout(10.seconds)
        .retryForeverExponential(10.seconds, maxDelay = 10.seconds),
      alternateDbRecovery =
        ErrorStrategies.timeout(10.seconds).retryCountFixed(3, 3.seconds)
    )
  )

}
