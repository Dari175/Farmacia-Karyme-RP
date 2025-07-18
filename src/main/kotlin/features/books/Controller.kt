package mx.com.lennux.features.books

import io.javalin.apibuilder.CrudHandler
import io.javalin.http.Context
import io.javalin.http.bodyValidator
import mx.com.lennux.DatabaseConnections
import mx.com.lennux.Util.properTrim
import mx.com.lennux.Util.randomUUID
import java.util.concurrent.CompletableFuture.supplyAsync

object BooksController: CrudHandler {

    private val service = BookService(DatabaseConnections.Maindb)


    override fun create(ctx: Context) {
        ctx.bodyValidator<Book>().get().apply {
            id = randomUUID()
            title = title.properTrim()
        }.also { book ->
            ctx.future {
                supplyAsync { service.insert(book) }
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
        ctx.bodyValidator<Book>().get().also { book ->
            ctx.future {
                supplyAsync { service.update(book) }
                    .thenAccept(ctx::json)
            }
        }
    }

}
