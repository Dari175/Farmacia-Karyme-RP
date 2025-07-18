package mx.com.lennux.features.mice

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import mx.com.lennux.DatabaseConnections
import mx.com.lennux.Util.properTrim
import mx.com.lennux.Util.randomUUID
import java.util.concurrent.CompletableFuture.supplyAsync

object MiceController : CrudHandler {

    private val service = MiceService(DatabaseConnections.Maindb)

    override fun create(ctx: Context) {
        ctx.bodyValidator<Mouse>().get().apply {
            id = randomUUID()
            name = name.properTrim()
        }.also { mouse ->
            ctx.future {
                supplyAsync { service.insert(mouse) }
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
        ctx.bodyValidator<Mouse>().get().also { mouse ->
            ctx.future {
                supplyAsync { service.update(mouse) }
                    .thenAccept(ctx::json)
            }
        }
    }

}
