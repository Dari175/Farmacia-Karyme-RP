package features.desserts

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import mx.com.lennux.DatabaseConnections
import mx.com.lennux.Util.properTrim
import mx.com.lennux.Util.randomUUID
import java.util.concurrent.CompletableFuture.supplyAsync

object DessertController: CrudHandler {

    private val service = DessertService(DatabaseConnections.Maindb)

    override fun create(ctx: Context) {
        ctx.bodyValidator<Dessert>().get().apply {
            id = randomUUID()
            name = name.properTrim()
        }.also { dessert ->
            ctx.future {
                supplyAsync { service.insert(dessert) }
                    .thenAccept ( ctx::result )
            }
        }
    }

    override fun delete(ctx: Context, resourceId: String) {
        ctx.future {
            supplyAsync { service.delete(resourceId) }
                .thenAccept ( ctx::json )
        }
    }

    override fun getAll(ctx: Context) {
        ctx.future {
            supplyAsync { service.selectAll() }
                .thenAccept ( ctx::json )
        }      }

    override fun getOne(ctx: Context, resourceId: String) {
        ctx.future {
            supplyAsync { service.selectOne(resourceId) }
                .thenAccept ( ctx::json )
        }
    }

    override fun update(ctx: Context, resourceId: String) {
        ctx.bodyValidator<Dessert>().get().also { dessert ->
            ctx.future {
                supplyAsync { service.update(dessert) }
                    .thenAccept(ctx::json)
            }
        }
    }
}