package features.desserts

import io.javalin.http.BadRequestResponse
import kotliquery.queryOf
import kotliquery.sessionOf
import mx.com.lennux.Util.dbErrorHandler
import mx.com.lennux.Util.toMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException
import javax.sql.DataSource

class DessertService(private val dataSource: DataSource) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)


    fun selectAll(): List<Dessert> {
        val query = queryOf(
            """
            SELECT 
                UUID_TO_CHAR(r.ID) ID, 
                r.DNAME NAME, 
                r.CALORIES, 
                r.FAT, 
                r.CARBS, 
                r.PROTEIN
            FROM DESSERTS r
        """.trimIndent()
        ).map(::rowToDessert).asList

        val result: List<Dessert>
        sessionOf(dataSource).use { conn ->
            result = conn.run(query)
        }

        return result
    }

    fun selectOne(id: String): Dessert {
        val query = queryOf(
            """
            SELECT 
                UUID_TO_CHAR(r.ID) ID, 
                r.DNAME NAME, 
                r.CALORIES, 
                r.FAT, 
                r.CARBS, 
                r.PROTEIN
            FROM DESSERTS r
            WHERE ID = CHAR_TO_UUID(?)
        """.trimIndent(), id
        ).map(::rowToDessert).asSingle

        val result: Dessert
        sessionOf(dataSource).use { conn ->
            result = conn.run(query) ?: throw BadRequestResponse("This dessert don't exists")
        }
        return result
    }

    fun insert(dessert: Dessert): String {
        val query = queryOf(
            """
            INSERT INTO DESSERTS (ID, DNAME, CALORIES, FAT, CARBS, PROTEIN)
            VALUES (CHAR_TO_UUID(:id), :name, :calories, :fat, :carbs, :protein)
        """.trimIndent(),
            toMap(dessert)
        )
        var result = "failed"
        sessionOf(dataSource).use { conn ->
            try{
                result =
                    if (conn.run(query.asUpdate) > 0) dessert.id.toString() else throw BadRequestResponse("Cannot add this element")
            } catch (ex: SQLException){
                dbErrorHandler(log, ex.message)

            }

        }
        return result
    }

    fun delete(id: String): Boolean {
        val query = queryOf(
            """
            DELETE FROM DESSERTS
            WHERE
            ID = CHAR_TO_UUID(?)
        """.trimIndent(), id
        )
        var result = false
        sessionOf(dataSource).use { conn ->
            result = if (conn.run(query.asUpdate) > 0) true else throw BadRequestResponse("This element was not deleted")
        }

        return result
    }

    fun update(dessert: Dessert): Boolean {
        val query = queryOf("""
            UPDATE DESSERTS 
            SET 
                DNAME = :name, 
                CALORIES = :calories, 
                FAT = :fat, 
                CARBS = :carbs, 
                PROTEIN = :protein
            WHERE
                ID = CHAR_TO_UUID(:id)
        """.trimIndent(),
                toMap(dessert)
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