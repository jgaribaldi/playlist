package playlist.domain

import java.time.Instant

class PlaylistWasDeleted(
    private val playlistWasDeletedData: PlaylistWasDeletedData,
    version: Long
): Event<Playlist>(Instant.now(), playlistWasDeletedData.id, version) {

    override fun apply(source: Playlist?): Nothing? =
        when {
            source == null ->
                throw RuntimeException("Can't apply deleted event to null source value")
            this.playlistWasDeletedData.id != source.id ->
                throw RuntimeException("Event belongs to different aggregate")
            else -> null
        }
}

data class PlaylistWasDeletedData(
    val id: String
)