package mx.com.lennux.features.mice

import io.javalin.http.BadRequestResponse
import kotliquery.queryOf
import kotliquery.sessionOf
import mx.com.lennux.Util.dbErrorHandler
import mx.com.lennux.Util.toMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException
import javax.sql.DataSource

class MiceService(private val dataSource: DataSource) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)


    fun selectAll(): List<Mouse> {
        val query = queryOf(
            """
                    SELECT 
                        UUID_TO_CHAR(ID) ID, 
                        NAME, 
                        COLOR, 
                        DPI, 
                        BUTTONS, 
                        WEIGHT, 
                        WIRELESS, 
                        PRICE,
                        DESCRIPTION, 
                        SRC
                    FROM 
                        MICE
        """.trimIndent()
        ).map(::rowToMouse).asList

        val result: List<Mouse>
        sessionOf(dataSource).use { conn ->
            result = conn.run(query)
        }

        return result
    }

    fun selectOne(id: String): Mouse {
        val query = queryOf(
            """
                     SELECT 
                        UUID_TO_CHAR(ID) ID, 
                        NAME, 
                        COLOR, 
                        DPI, 
                        BUTTONS, 
                        WEIGHT, 
                        WIRELESS, 
                        PRICE,
                        DESCRIPTION, 
                        SRC
                    FROM 
                        MICE
                    WHERE
                        ID = CHAR_TO_UUID(?)
        """.trimIndent(), id
        ).map(::rowToMouse).asSingle

        val result: Mouse
        sessionOf(dataSource).use { conn ->
            result = conn.run(query) ?: throw BadRequestResponse("This mouse don't exists")
        }
        return result
    }

    fun insert(mouse: Mouse): String {
        val query = queryOf(
            """
            INSERT INTO MICE 
                (ID, NAME, COLOR, DPI, BUTTONS, WEIGHT, WIRELESS, PRICE, DESCRIPTION, SRC)
            VALUES (CHAR_TO_UUID(:id),:name,:color,:dpi,:buttons,:weight, :wireless, :price, :description,:src);

        """.trimIndent(),
            toMap(mouse)
        )
        var result = "failed"
        sessionOf(dataSource).use { conn ->
            try {
                result =
                    if (conn.run(query.asUpdate) > 0) mouse.id.toString() else throw BadRequestResponse("Cannot add this element")
            } catch (ex: SQLException){
                dbErrorHandler(log, ex.message)

            }
        }
        return result
    }

    fun delete(id: String): Boolean {
        val query = queryOf(
            """
            DELETE FROM MICE
            WHERE
            ID = CHAR_TO_UUID(?)
        """.trimIndent(), id
        )
        var result = false
        sessionOf(dataSource).use { conn ->
            result =
                if (conn.run(query.asUpdate) > 0) true else throw BadRequestResponse("This element was not deleted")
        }

        return result
    }

    fun update(mouse: Mouse): Boolean {
        val query = queryOf(
            """
            UPDATE MICE
            SET 
                NAME = :name, 
                COLOR = :color, 
                DPI = :dpi, 
                BUTTONS = :buttons, 
                WEIGHT = :weight, 
                WIRELESS = :wireless, 
                PRICE = :price, 
                DESCRIPTION = :description, 
                SRC = :src
            WHERE
                ID = CHAR_TO_UUID(:id)

        """.trimIndent(),
            toMap(mouse)
            )

        var result = false

        sessionOf(dataSource).use { conn ->
            try {
                result =
                    if (conn.run(query.asUpdate) > 0) true else throw BadRequestResponse("Cannot update this element")
            } catch (ex: SQLException){
                dbErrorHandler(log, ex.message)

            }
        }
        return result
    }

}
