package de.tfr.game.server

import de.tfr.game.server.plugins.configureRouting
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.Test
import kotlin.test.assertEquals

class ApplicationTest {
  @Test
  fun testRoot() {
    withTestApplication({ configureRouting() }) {
      handleRequest(HttpMethod.Get, "/").apply {
        assertEquals(HttpStatusCode.OK, response.status())
        assertEquals("KoJa Match Up Game Server ", response.content)
      }
    }
  }
}