package org.baris.test

import com.zaxxer.hikari.{HikariConfig, HikariDataSource}
import zio.blocking.Blocking
import zio.{Has, ZIO, ZLayer, blocking}

import javax.sql.DataSource

object ConnectionPool {

  val live: ZLayer[Conf with Blocking, Throwable, Has[DataSource]] =
    ZIO
      .accessM[Conf with Blocking] { env =>
        val conf = env.get[Conf.Root]
        blocking.effectBlocking {
          val config = new HikariConfig()
          config.setJdbcUrl(conf.db.url)
          config.setUsername(conf.db.username)
          val dataSource = new HikariDataSource(config)
          dataSource
        }
      }
      .toLayer

}
