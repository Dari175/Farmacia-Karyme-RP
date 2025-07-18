package mx.com.lennux.features.games

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import mx.com.lennux.DatabaseConnections
import mx.com.lennux.Util.properTrim
import mx.com.lennux.Util.randomUUID
import java.util.concurrent.CompletableFuture.supplyAsync

object GamesController: CrudHandler {

    private val service = GameService(DatabaseConnections.Maindb)

    override fun create(ctx: Context) {
        ctx.bodyValidator<Game>().get().apply {
            id = randomUUID()
            title = title.properTrim()
        }.also { game ->
            ctx.future {
                supplyAsync { service.insert(game) }
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
        ctx.bodyValidator<Game>().get().also { game ->
            ctx.future {
                supplyAsync { service.update(game) }
                    .thenAccept(ctx::json)
            }
        }
    }
}