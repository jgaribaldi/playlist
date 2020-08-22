package playlist.commands

import playlist.domain.EventRepository
import playlist.domain.Playlist
import playlist.domain.SongWasRemoved
import playlist.domain.SongWasRemovedData

class RemoveSong(
    private val eventRepository: EventRepository<Playlist>
): Command<RemoveSongData>() {

    override operator fun invoke(commandData: RemoveSongData) {
        val sortedEvents = eventRepository.getByAggregateId(commandData.playlistId)
        val playlist = Playlist.fromEvents(sortedEvents)
            ?: throw RuntimeException("Playlist does not exist")

        if (!playlist.songList.contains(commandData.songName)) {
            throw RuntimeException("Given song not present in given playlist")
        }

        val songWasRemovedData = SongWasRemovedData(
            aggregateId = commandData.playlistId,
            songName = commandData.songName
        )
        val songWasRemoved = SongWasRemoved(songWasRemovedData, playlist.version + 1)
        eventRepository.save(songWasRemoved)
    }
}

data class RemoveSongData(
    val playlistId: String,
    val songName: String
)