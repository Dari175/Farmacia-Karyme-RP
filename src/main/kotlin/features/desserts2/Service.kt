package features.desserts2

import io.javalin.http.BadRequestResponse
import kotliquery.queryOf
import kotliquery.sessionOf
import mx.com.lennux.Util.dbErrorHandler
import mx.com.lennux.Util.toMap
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.sql.SQLException
import javax.sql.DataSource

class IDessertService(private val dataSource: DataSource) {

    val log: Logger = LoggerFactory.getLogger(this::class.java)

    fun selectAll(): List<Dessert> {
        val query = queryOf(
            """
            SELECT 
                UUID_TO_CHAR(ID) AS ID, 
                DNAME AS NAME, 
                DESCRIPTION, 
                ICON, 
                COLOR, 
                CALORIES, 
                FAT, 
                CARBS, 
                PROTEIN, 
                SODIUM, 
                CALCIUM, 
                IRON
            FROM IDESSERTS
            """.trimIndent()
        ).map(::rowToDessert).asList

        return sessionOf(dataSource).use { conn -> conn.run(query) }
    }

    fun selectOne(id: String): Dessert {
        val query = queryOf(
            """
            SELECT 
                UUID_TO_CHAR(ID) AS ID, 
                DNAME AS NAME, 
                DESCRIPTION, 
                ICON, 
                COLOR, 
                CALORIES, 
                FAT, 
                CARBS, 
                PROTEIN, 
                SODIUM, 
                CALCIUM, 
                IRON
            FROM IDESSERTS
            WHERE ID = CHAR_TO_UUID(?)
            """.trimIndent(), id
        ).map(::rowToDessert).asSingle

        return sessionOf(dataSource).use { conn ->
            conn.run(query) ?: throw BadRequestResponse("This dessert doesn't exist")
        }
    }

    fun insert(dessert: Dessert): String {
        val query = queryOf(
            """
            INSERT INTO IDESSERTS (
                ID, DNAME, DESCRIPTION, ICON, COLOR, 
                CALORIES, FAT, CARBS, PROTEIN, 
                SODIUM, CALCIUM, IRON
            )
            VALUES (
                CHAR_TO_UUID(:id), :name, :description, :icon, :color, 
                :calories, :fat, :carbs, :protein, 
                :sodium, :calcium, :iron
            )
            """.trimIndent(),
            toMap(dessert)
        )

        return sessionOf(dataSource).use { conn ->
            try {
                if (conn.run(query.asUpdate) > 0) {
                    dessert.id.toString()
                } else {
                    throw BadRequestResponse("Cannot add this element")
                }
            } catch (ex: SQLException) {
                dbErrorHandler(log, ex.message)
                "failed"
            }
        }
    }

    fun update(dessert: Dessert): Boolean {
        val query = queryOf(
            """
            UPDATE IDESSERTS 
            SET 
                DNAME = :name, 
                DESCRIPTION = :description, 
                ICON = :icon, 
                COLOR = :color, 
                CALORIES = :calories, 
                FAT = :fat, 
                CARBS = :carbs, 
                PROTEIN = :protein, 
                SODIUM = :sodium, 
                CALCIUM = :calcium, 
                IRON = :iron
            WHERE ID = CHAR_TO_UUID(:id)
            """.trimIndent(),
            toMap(dessert)
        )

        return sessionOf(dataSource).use { conn ->
            try {
                if (conn.run(query.asUpdate) > 0) true
                else throw BadRequestResponse("Cannot update this element")
            } catch (ex: SQLException) {
                dbErrorHandler(log, ex.message)
                false
            }
        }
    }

    fun delete(id: String): Boolean {
        val query = queryOf(
            """
            DELETE FROM IDESSERTS
            WHERE ID = CHAR_TO_UUID(?)
            """.trimIndent(), id
        )

        return sessionOf(dataSource).use { conn ->
            if (conn.run(query.asUpdate) > 0) true
            else throw BadRequestResponse("This element was not deleted")
        }
    }
}
