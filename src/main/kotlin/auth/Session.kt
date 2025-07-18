package mx.com.lennux.auth


import kotliquery.HikariCP
import org.eclipse.jetty.http.HttpCookie
import org.eclipse.jetty.server.session.SessionHandler
import org.eclipse.jetty.server.session.FileSessionDataStore
import org.eclipse.jetty.server.session.DatabaseAdaptor
import org.eclipse.jetty.server.session.JDBCSessionDataStoreFactory
import org.eclipse.jetty.server.session.NullSessionCache
import java.io.File


object Session {

    fun fileSessionHandler() = SessionHandler().apply {
        sessionCache = NullSessionCache(this).apply {
            sessionDataStore = FileSessionDataStore().apply {
                // val baseDir = File(System.getProperty("java.io.tmpdir"))
                val baseDir = File("config")
                this.storeDir = File(baseDir, "javalin-session-store").apply{ mkdir()}
            }
        }

        httpOnly = true
        isSecureRequestOnly = true
        sameSite = HttpCookie.SameSite.STRICT
    }

    fun sqlSessionHandler() = SessionHandler().apply {
        sessionCache = NullSessionCache(this).apply {
            // sessionCache = DefaultSessionCache(this).apply {
            sessionDataStore = JDBCSessionDataStoreFactory().apply {
                setDatabaseAdaptor(DatabaseAdaptor().apply {
                    datasource = HikariCP.dataSource()
                })
            }.getSessionDataStore(sessionHandler)
        }

        httpOnly = true
        isSecureRequestOnly = true
        sameSite = HttpCookie.SameSite.STRICT
    }
}
