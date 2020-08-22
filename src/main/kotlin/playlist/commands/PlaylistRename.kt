package playlist.commands

import playlist.domain.EventRepository
import playlist.domain.Playlist
import playlist.domain.PlaylistWasRenamed
import playlist.domain.PlaylistWasRenamedData

class RenamePlaylist(
    private val eventRepository: EventRepository<Playlist>
): Command<RenamePlaylistData>() {

    override operator fun invoke(commandData: RenamePlaylistData) {
        val sortedEvents = eventRepository.getByAggregateId(commandData.id)
        val playlist = Playlist.fromEvents(sortedEvents)
            ?: throw RuntimeException("Playlist does not exist")

        val playlistWasRenamedData = PlaylistWasRenamedData(
            id = commandData.id,
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