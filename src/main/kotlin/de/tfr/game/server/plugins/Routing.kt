package de.tfr.game.server.plugins

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Application.configureRouting() {
    routing {
        get("/") {
            val appVersion = System.getenv("appVersion").orEmpty()
            call.respondText("KoJa Match Up Game Server $appVersion")
        }
    }
}
