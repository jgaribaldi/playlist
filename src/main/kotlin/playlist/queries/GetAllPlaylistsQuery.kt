package playlist.queries

import playlist.domain.EventRepository
import playlist.domain.Playlist

class GetAllPlaylistsQuery(
    private val eventRepository: EventRepository<Playlist>
) {
    operator fun invoke(): List<Playlist> =
        eventRepository.getAll()
            .groupBy { it.aggregateId }
            .filterValues { Playlist.fromEvents(it) != null }
            .map { Playlist.fromEvents(it.value)!! }
}