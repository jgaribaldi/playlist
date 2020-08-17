package playlist.commands

import playlist.domain.EventRepository
import playlist.domain.Playlist
import playlist.domain.PlaylistWasRenamed
import playlist.domain.PlaylistWasRenamedData
import java.lang.RuntimeException
import java.util.*

class RenamePlaylist(
    private val eventRepository: EventRepository<Playlist>
): Command<RenamePlaylistData>() {

    override operator fun invoke(commandData: RenamePlaylistData) {
        val sortedEvents = eventRepository.getByAggregateId(commandData.id)
        val playlist = Playlist.fromEvents(sortedEvents)
            ?: throw RuntimeException("Playlist does not exist")

        val aggregateId = UUID.fromString(commandData.id)!!
        val playlistWasRenamedData = PlaylistWasRenamedData(
            id = aggregateId,
            newName = commandData.newName
        )
        val playlistWasRenamed = PlaylistWasRenamed(playlistWasRenamedData, playlist.version + 1)
        eventRepository.save(playlistWasRenamed)
    }
}

data class RenamePlaylistData(
    val id: String,
    val newName: String
)