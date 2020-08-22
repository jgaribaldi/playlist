package playlist.commands

import playlist.domain.EventRepository
import playlist.domain.Playlist
import playlist.domain.PlaylistWasDeleted
import playlist.domain.PlaylistWasDeletedData

class DeletePlaylist(
    private val eventRepository: EventRepository<Playlist>
): Command<DeletePlaylistData>() {

    override operator fun invoke(commandData: DeletePlaylistData) {
        val sortedEvents = eventRepository.getByAggregateId(commandData.id)
        val playlist = Playlist.fromEvents(sortedEvents)
            ?: throw RuntimeException("Playlist does not exist")

        val playlistWasDeletedData = PlaylistWasDeletedData(id = commandData.id)
        val playlistWasDeleted = PlaylistWasDeleted(playlistWasDeletedData, playlist.version + 1)
        eventRepository.save(playlistWasDeleted)
    }
}

data class DeletePlaylistData(val id: String)