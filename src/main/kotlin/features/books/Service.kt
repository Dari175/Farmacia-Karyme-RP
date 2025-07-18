package mx.com.lennux.features.books

import io.javalin.http.BadRequestResponse
import kotliquery.queryOf
import kotliquery.sessionOf
import mx.com.lennux.Util.dbErrorHandler
import mx.com.lennux.Util.toMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException
import javax.sql.DataSource

class BookService(private val dataSource: DataSource) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    
    fun selectAll(): List<Book> {
        val query = queryOf(
            """
            SELECT 
                UUID_TO_CHAR(ID) ID, 
                TITLE, 
                AUTHOR, 
                GENRE, 
                BYEAR, 
                BPAGES
            FROM 
                BOOKS;
        """.trimIndent()
        ).map(::rowToBook).asList

        val result: List<Book>
        sessionOf(dataSource).use { conn ->
            result = conn.run(query)
        }

        return result
    }

    fun selectOne(id: String): Book {
        val query = queryOf(
            """
            SELECT 
                UUID_TO_CHAR(ID) ID, 
                TITLE, 
                AUTHOR, 
                GENRE, 
                BYEAR, 
                BPAGES
            FROM BOOKS
            WHERE ID = CHAR_TO_UUID(?)
        """.trimIndent(), id
        ).map(::rowToBook).asSingle

        val result: Book
        sessionOf(dataSource).use { conn ->
            result = conn.run(query) ?: throw BadRequestResponse("This dessert don't exists")
        }
        return result
    }

    fun insert(book: Book): String {
        val query = queryOf(
            """
            INSERT INTO BOOKS (ID, TITLE, AUTHOR, GENRE, BYEAR, BPAGES)
            VALUES (CHAR_TO_UUID(:id), :title, :author, :genre, :year, :pages);
        """.trimIndent(),
            toMap(book)
        )
        var result = "failed"
        sessionOf(dataSource).use { conn ->
            try {
                result =
                    if (conn.run(query.asUpdate) > 0) book.id.toString() else throw BadRequestResponse("Cannot add this element")
            } catch (ex: SQLException){
                dbErrorHandler(log, ex.message)

            }
        }
        return result
    }

    fun delete(id: String): Boolean {
        val query = queryOf(
            """
            DELETE FROM BOOKS
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

    fun update(book: Book): Boolean {
        val query = queryOf(
            """
                UPDATE BOOKS
                    SET
                    TITLE = :title,
                    AUTHOR = :author,
                    GENRE = :genre,
                    BYEAR = :year,
                    BPAGES = :pages
                WHERE
                    ID = CHAR_TO_UUID(:id)
        """.trimIndent(),
            toMap(book)
        )

        var result = false

        sessionOf(dataSource).use { conn ->
            try {
            result = if (conn.run(query.asUpdate) > 0) true else throw BadRequestResponse("Cannot update this element")
            } catch (ex: SQLException){
                dbErrorHandler(log, ex.message)

            }
        }
        return result
    }


}
