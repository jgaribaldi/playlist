package playlist.commands

import playlist.domain.EventRepository
import playlist.domain.Playlist
import playlist.domain.SongWasAdded
import playlist.domain.SongWasAddedData
import java.lang.RuntimeException
import java.util.*

class AddSong(
    private val eventRepository: EventRepository<Playlist>
): Command<AddSongData>() {

    override operator fun invoke(commandData: AddSongData) {
        val sortedEvents = eventRepository.getByAggregateId(commandData.playlistId)
        val playlist = Playlist.fromEvents(sortedEvents)
            ?: throw RuntimeException("Playlist does not exist")

        val aggregateId = UUID.fromString(commandData.playlistId)!!
        val songWasAddedData = SongWasAddedData(
            playlistId = aggregateId,
            songName = commandData.songName
        )
        val songWasAdded = SongWasAdded(songWasAddedData, playlist.version + 1)
        eventRepository.save(songWasAdded)
    }
}

data class AddSongData(
    val playlistId: String,
    val songName: String
)