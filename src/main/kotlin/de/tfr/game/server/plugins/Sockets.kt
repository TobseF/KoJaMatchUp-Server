package de.tfr.game.server.plugins

import de.tfr.game.server.PlayerConnection
import de.tfr.game.server.ScoreCounter
import de.tfr.kojamatch.game.network.*
import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.routing.*
import io.ktor.websocket.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

fun Application.configureSockets() {
  install(WebSockets) {
    pingPeriod = Duration.ofSeconds(15)
    timeout = Duration.ofSeconds(15)
    maxFrameSize = Long.MAX_VALUE
    masking = false
  }

  val json = Json
  val playerConnections = ConcurrentHashMap<Int, PlayerConnection>()

  fun listPlayerConnectionsExcept(connection: PlayerConnection) = playerConnections.asSequence().filter {
    it.key != connection.id
  }.filter { it.value.didHandshake() }.map { it.value }

  fun listPlayers(connection: PlayerConnection) = listPlayerConnectionsExcept(connection)
    .map { it.player }.filterNotNull().toList()

  fun PlayerPos.toPacket() = this.toMultiplayerEvent().packed()

  suspend fun WebSocketSession.send(packet: Packet) = this.send(json.encodeToString(packet))
  suspend fun WebSocketSession.send(event: GameEvent) = this.send(event.packed())
  suspend fun WebSocketSession.send(playerPos: PlayerPos) = this.send(playerPos.toPacket())
  suspend fun broadCast(event: GameEvent) = playerConnections.asSequence().forEach { it.value.session.send(event) }

  routing {
    webSocket("/game") {
      val playerConnection = PlayerConnection(this)
      val playerID = playerConnection.id
      playerConnections[playerID] = playerConnection
      println("Added player connection: $playerConnection")
      try {
        send(ConnectedEvent(playerID, ScoreCounter.get()))
        send(MultiplayerEvent(listPlayers(playerConnection)))

        for (frame in incoming) {
          when (frame) {
            is Frame.Text -> {
              val text = frame.readText()
              log.debug("Message from client $playerID: $text")
              val packet = json.decodeFromStringOrNull<Packet>(text)
              if (packet == null) {
                log.warn("Cannot parse: $text")
                continue
              }

              when (packet.clientEvent) {
                is NewPositionEvent -> {
                  val event = packet.clientEvent
                  playerConnection.setPos(event.pos)
                  listPlayerConnectionsExcept(playerConnection).forEach {
                    it.session.send(event.pos)
                  }
                }

                is NewCardEvent ->{
                  broadCast(NewScoreEvent(ScoreCounter.count(),packet.clientEvent.pos ))
                  log.debug("Broadcasting new score ${ScoreCounter.get()}")
                }
                else -> {
                  log.debug("Ignoring event ${packet.clientEvent}")
                }
              }
            }
            is Frame.Binary -> log.debug("Unknown frame: Binary")
            is Frame.Close -> log.debug("Unknown frame: Close")
            is Frame.Ping -> log.debug("Unknown frame: Ping")
            is Frame.Pong -> log.debug("Unknown frame: Pong")
          }
        }
      } catch (e: Exception) {
        log.warn("Connection of player " + playerID + " was killed: " + e.localizedMessage, e)
      } finally {
        playerConnections.remove(playerID)
        println("Removing $playerConnection")
        broadCast(PlayerGoneEvent(playerID))
      }

    }
  }
}
