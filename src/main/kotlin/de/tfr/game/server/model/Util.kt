package de.tfr.kojamatch.game.network

import kotlinx.serialization.json.Json
import kotlinx.serialization.serializer

inline fun <reified T> Json.decodeFromStringOrNull(string: String): T? {
  return try {
    decodeFromString(serializersModule.serializer(), string)
  } catch (e: Exception) {
    null
  }
}
