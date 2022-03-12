package de.tfr.game.server

import java.util.concurrent.atomic.AtomicInteger

object ScoreCounter {
  private val counter = AtomicInteger()

  fun count() = counter.incrementAndGet()

  fun get() = counter.get()
}