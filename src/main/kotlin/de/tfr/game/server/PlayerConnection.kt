package de.tfr.game.server

import de.tfr.kojamatch.game.network.PlayerPos
import io.ktor.http.cio.websocket.*
import java.util.concurrent.atomic.AtomicInteger

class PlayerConnection(val session: DefaultWebSocketSession) {
  companion object {
    val ID = AtomicInteger()
  }

  val id = ID.getAndIncrement()

  var player: PlayerPos? = null
    private set

  fun setPos(pos: PlayerPos) {
    player = PlayerPos(pos.x, pos.y, id)
  }

  fun didHandshake() = player != null

  override fun toString(): String {
    return "PlayerConnection(id=$id)"
  }
}