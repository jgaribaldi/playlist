package playlist

import playlist.domain.Event
import playlist.domain.EventRepository
import playlist.domain.Playlist
import playlist.infrastructure.Observable

class InMemoryEventRepository: EventRepository<Playlist>, Observable<Event<Playlist>>() {
    private val events: MutableList<Event<Playlist>> = mutableListOf()

    override fun save(event: Event<Playlist>) {
        // concurrency control by versioning - must be supported by the event store
        val existingRecords = getByAggregateId(event.aggregateId)
        val latestVersion = existingRecords.maxByOrNull { it.version } ?.version ?: -1

        if (event.version == latestVersion + 1) {
            events.add(event)
            updateObservers(event)  // notify observers of events
        } else {
            throw RuntimeException("Wrong version - Cannot save event")
        }
    }

    override fun getByAggregateId(aggregateId: String) = events.filter { it.aggregateId == aggregateId }

    override fun getAll(): List<Event<Playlist>> = events.toMutableList().toList()
}