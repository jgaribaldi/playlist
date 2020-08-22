package playlist.domain

import java.time.Instant

class PlaylistWasRenamed(
    private val playlistWasRenamedData: PlaylistWasRenamedData,
    version: Long
): Event<Playlist>(Instant.now(), playlistWasRenamedData.id, version) {

    override fun apply(source: Playlist?): Playlist? {
        if (source == null) {
            throw RuntimeException("Can't apply renamed event to null source value")
        } else {
            return source.copy(
                name = playlistWasRenamedData.newName,
                version = source.version + 1
            )
        }
    }
}

data class PlaylistWasRenamedData(
    val id: String,
    val newName: String
)