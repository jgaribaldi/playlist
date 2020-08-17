package playlist.domain

import java.util.*

data class Playlist(
    val id: UUID,
    val name: String,
    val owner: String,
    val songList: List<String>,
    val version: Long
) {
    companion object {
        fun fromEvents(
            sortedEvents: List<Event<Playlist>>
        ): Playlist? {
            val initial: Playlist? = null
            return sortedEvents.fold(initial, { partial, event -> event.apply(partial) })
        }
    }
}