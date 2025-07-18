package mx.com.lennux

import features.desserts.DessertController
import io.javalin.Javalin
import io.javalin.apibuilder.ApiBuilder.crud
import io.javalin.apibuilder.ApiBuilder.path
import io.javalin.apibuilder.ApiBuilder.get
import io.javalin.http.staticfiles.Location
import io.javalin.vue.VueComponent
import features.desserts2.IDessertController
import mx.com.lennux.auth.AccessManager.userInfo
import mx.com.lennux.features.books.BooksController
import mx.com.lennux.features.games.GamesController
import mx.com.lennux.features.mice.MiceController

fun main() {

//    HikariCP.default(
//        url = SRV_CONF.dbUrl,
//        username = SRV_CONF.dbUser,
//        password = SRV_CONF.dbPwd )

    val app = Javalin.create { config ->

        //config.jetty.modifyServletContextHandler { it.sessionHandler = Session.sqlSessionHandler() }

        config.staticFiles.apply{
            enableWebjars()
            add(
                directory = "public",
                location = Location.CLASSPATH
            )

        }
        config.vue.apply{
            vueInstanceNameInJs = "app"
            rootDirectory(
                path = "/vue",
                location = Location.CLASSPATH
            )
            stateFunction = { ctx -> mapOf("userInfo" to ctx.userInfo)}
        }
        config.router.mount{}.apiBuilder {
            get("/", VueComponent("welcome-page") )
            get ("/Antidoping", VueComponent("antidoping-page") )
            get ("/Alcoh`olimetro", VueComponent("alcoholimetro-page"))



        }
    }
//        .exception(ValidationException::class.java) { e, ctx ->
//        val error = e.errors.values.single().joinToString { it.message }
//        ctx.result(error).status(500)
//    }
        .start()
}