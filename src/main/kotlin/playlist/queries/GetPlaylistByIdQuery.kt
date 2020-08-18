package playlist.queries

import playlist.domain.EventRepository
import playlist.domain.Playlist

class GetPlaylistByIdQuery(
    private val eventRepository: EventRepository<Playlist>
) {
    operator fun invoke(playlistId: String): Playlist? {
        val events = eventRepository.getByAggregateId(playlistId)
        return if (events.isNotEmpty()) {
            Playlist.fromEvents(events)
        } else {
            null
        }
    }
}