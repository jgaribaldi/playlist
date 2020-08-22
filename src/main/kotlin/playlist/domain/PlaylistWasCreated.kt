package playlist.domain

import java.lang.RuntimeException
import java.time.Instant
import java.util.*

class PlaylistWasCreated(
    private val playlistWasCreatedData: PlaylistWasCreatedData,
    version: Long
): Event<Playlist>(Instant.now(), playlistWasCreatedData.id, version) {

    override fun apply(source: Playlist?): Playlist {
        if (source != null) {
            throw RuntimeException("Can't apply created event to non-null source value")
        } else {
            return Playlist(
                id = this.playlistWasCreatedData.id,
                name = this.playlistWasCreatedData.name,
                owner = this.playlistWasCreatedData.owner,
                songList = listOf(),
                version = 0
            )
        }
    }
}

data class PlaylistWasCreatedData(
    val id: String,
    val name: String,
    val owner: String
)