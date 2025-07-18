package mx.com.lennux.features.games

import io.javalin.http.BadRequestResponse
import kotliquery.queryOf
import kotliquery.sessionOf
import mx.com.lennux.Util.dbErrorHandler
import mx.com.lennux.Util.toMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException
import javax.sql.DataSource

class GameService(private val dataSource: DataSource) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)


    fun selectAll(): List<Game> {
        val query = queryOf(
            """
            SELECT UUID_TO_CHAR(a.ID) ID, a.TITLE, a.SUBTITLE, a.ADVANCED, a.DURATION, a.IMG
FROM GAMES a 
        """.trimIndent()
        ).map(::rowToGame).asList

        val result: List<Game>
        sessionOf(dataSource).use { conn ->
            result = conn.run(query)
        }

        return result
    }

    fun selectOne(id: String): Game {
        val query = queryOf(
            """
           SELECT UUID_TO_CHAR(a.ID) ID, a.TITLE, a.SUBTITLE, a.ADVANCED, a.DURATION, a.IMG
                FROM GAMES a 
           WHERE
                a.ID = CHAR_TO_UUID(?)
        """.trimIndent(), id
        ).map(::rowToGame).asSingle

        val result: Game
        sessionOf(dataSource).use { conn ->
            result = conn.run(query) ?: throw BadRequestResponse("This dessert don't exists")
        }
        return result
    }

    fun insert(game: Game): String {
        val query = queryOf(
            """
            INSERT INTO GAMES (ID, TITLE, SUBTITLE, ADVANCED, DURATION, IMG)
VALUES (
    CHAR_TO_UUID(:id), :title, :subtitle, :advanced, :duration, :img );
        """.trimIndent(),
            toMap(game)
        )
        var result = "failed"
        sessionOf(dataSource).use { conn ->
                try {
                    result =
                        if (conn.run(query.asUpdate) > 0) game.id.toString() else throw BadRequestResponse("Cannot add this element")
                } catch (ex: SQLException) {
                    dbErrorHandler(log, ex.message)
                }
        }
        return result
    }

    fun delete(id: String): Boolean {
        val query = queryOf(
            """
            DELETE FROM GAMES
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

    fun update(game: Game): Boolean {
        val query = queryOf(
            """
            UPDATE GAMES a
SET 
    a.TITLE = :title, 
    a.SUBTITLE = :subtitle, 
    a.ADVANCED = :advanced, 
    a.DURATION = :duration, 
    a.IMG = :img
WHERE
    a.ID = CHAR_TO_UUID(:id)
        """.trimIndent(),
            toMap(game)

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