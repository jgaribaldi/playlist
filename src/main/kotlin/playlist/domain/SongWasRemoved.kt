package playlist.domain

import java.time.Instant

class SongWasRemoved(
    private val songWasRemovedData: SongWasRemovedData,
    version: Long
): Event<Playlist>(Instant.now(), songWasRemovedData.playlistId, version) {

    override fun apply(source: Playlist?): Playlist? {
        if (source == null) {
            throw RuntimeException("Can't apply song was removed event to null source value")
        } else {
            val newSongList = source.songList.toMutableList()
            newSongList.remove(songWasRemovedData.songName)
            return source.copy(
                songList = newSongList,
                version = source.version + 1
            )
        }
    }
}

data class SongWasRemovedData(
    val playlistId: String,
    val songName: String
)