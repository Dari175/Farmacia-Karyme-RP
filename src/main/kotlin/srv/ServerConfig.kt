package mx.com.lennux.srv

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.FileInputStream
import java.util.Properties

data class ServerConfig(
    val jdbcProtocol: String,
    val dbHost: String,
    val dbPort: String,
    val dbName: String,
    val dbUser: String,
    val dbPwd: String,
    val dbEncoding: String,
) {
    val dbUrl get() = "$jdbcProtocol://$dbHost:$dbPort/$dbName?encoding=$dbEncoding"
}

object dbConfig {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    val SRV_CONF : ServerConfig

    init {
        val prop = Properties().apply { load(FileInputStream("config/server.properties")) }

        SRV_CONF = ServerConfig(
            jdbcProtocol = prop.getProperty("database.jdbc.protocol"),
            dbHost = prop.getProperty("database.host"),
            dbPort = prop.getProperty("database.port"),
            dbName = prop.getProperty("database.name"),
            dbUser = prop.getProperty("database.user"),
            dbPwd = prop.getProperty("database.password"),
            dbEncoding = prop.getProperty("database.encoding")
        )
        log.info("Database Url: {}", SRV_CONF.dbUrl)
        log.info("Database user {}", SRV_CONF.dbUser)
    }
}
