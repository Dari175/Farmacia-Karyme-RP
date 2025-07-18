package mx.com.lennux

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

object DatabaseConnections {

    val Maindb = createDataSource("jdbc:firebird://localhost:3051/dessert?encoding=UTF8")

    private fun createDataSource(url: String): HikariDataSource {
        val config = HikariConfig().apply {
            jdbcUrl = url
            username = "sysdba"
            password = "M4st3rK3y"
            driverClassName = "org.firebirdsql.jdbc.FBDriver"
            maximumPoolSize = 5
        }
        return HikariDataSource(config)
    }
}