package de.tfr.game.server

import de.tfr.game.server.plugins.configureHTTP
import de.tfr.game.server.plugins.configureRouting
import de.tfr.game.server.plugins.configureSecurity
import de.tfr.game.server.plugins.configureSockets
import io.ktor.server.cio.*
import io.ktor.server.engine.*

fun main() {
    embeddedServer(CIO, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureRouting()
        configureSockets()
        configureSecurity()
    }.start(wait = true)
}
