package playlist.commands

import playlist.domain.EventRepository
import playlist.domain.Playlist
import playlist.domain.PlaylistWasCreated
import playlist.domain.PlaylistWasCreatedData

class CreatePlaylist(
    private val eventRepository: EventRepository<Playlist>
): Command<CreatePlaylistData>() {

    override operator fun invoke(commandData: CreatePlaylistData) {
        // TODO: validate

        val playlistWasCreatedData = PlaylistWasCreatedData(
            name = commandData.name,
            owner = commandData.owner,
            id = commandData.id
        )
        val event = PlaylistWasCreated(playlistWasCreatedData, 0)
        eventRepository.save(event)
    }
}

data class CreatePlaylistData(
    val name: String,
    val owner: String,
    val id: String
)