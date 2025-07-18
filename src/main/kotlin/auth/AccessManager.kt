package mx.com.lennux.auth

import io.javalin.http.BadRequestResponse
import io.javalin.http.Context
import io.javalin.http.UnauthorizedResponse
import io.javalin.security.RouteRole
import java.io.Serializable


enum class Role: RouteRole {
    SYSADMIN, ADMIN, CLIENT, UNAUTHENTICATED
}

object AccessManager {

    fun HandlerAccess(ctx: Context) {
        if (ctx.matchedPath() != "/api/image") ctx.refreshUserInfo()
        val permittedRoles = ctx.routeRoles()

        when {
            Role.UNAUTHENTICATED in permittedRoles || permittedRoles.isEmpty() -> return
            ctx.userInfo == null -> ctx.redirect("login")
            ctx.userInfo!!.role in permittedRoles -> return
            else -> throw UnauthorizedResponse("this user is not authorized to this content")
        }
    }

    private fun Context.refreshUserInfo() {
        userInfo?.let {
//            val acc = userAccountService.userInfoBy("ID", it.id).apply { role = it.role }
//            userInfo = if(acc.active) acc else null
        }
    }


    data class userInfo(
        val id: String,
        val userName: String,
        var password: String,
        val fullName: String,
        var role: Role = Role.UNAUTHENTICATED,
        val roles: List<String> = listOf(),
        val active: Boolean = false,
        val currentData: String) : Serializable {
            val photo = "api/image?url=photos/users/$id-mini.png"

        fun mapRole (strRole: String) {
            roles.firstOrNull { it.equals(strRole, true)}?.also {
                role = when(it){
                    "SysAdmin" -> Role.SYSADMIN
                    "Admin" -> Role.ADMIN
                    "Cliente" -> Role.CLIENT
                    else -> Role.UNAUTHENTICATED
                }
            } ?: throw UnauthorizedResponse("this user is not authorized to this content")
            password = "*********************"
        }


        }

    var Context.userInfo: userInfo?
        get() = this.sessionAttribute("USER_INFO")
        set(userInfo) = this.sessionAttribute("USER_INFO", userInfo)
}