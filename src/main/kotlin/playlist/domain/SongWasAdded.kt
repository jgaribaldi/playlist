package playlist.domain

import java.time.Instant
import java.util.*

class SongWasAdded(
    private val songWasAddedData: SongWasAddedData,
    version: Long
): Event<Playlist>(Instant.now(), songWasAddedData.playlistId, version) {

    override fun apply(source: Playlist?): Playlist? {
        if (source == null) {
            throw RuntimeException("Can't apply song added event to null source value")
        } else {
            val newSongList = source.songList.toMutableList()
            newSongList.add(songWasAddedData.songName)
            return source.copy(
                songList = newSongList.toList(),
                version = source.version + 1
            )
        }
    }
}

data class SongWasAddedData(
    val playlistId: UUID,
    val songName: String
)