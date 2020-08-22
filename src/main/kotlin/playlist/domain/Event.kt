package playlist.domain

import java.time.Instant
import java.util.*

abstract class Event<T>(
    val timestamp: Instant,
    val aggregateId: String,
    val version: Long
) {
    abstract fun apply(source: T?): T?
}
