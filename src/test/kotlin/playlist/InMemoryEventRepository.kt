package playlist

import playlist.domain.Event
import playlist.domain.EventRepository
import playlist.domain.Playlist
import java.lang.RuntimeException
import java.util.*

class InMemoryEventRepository: EventRepository<Playlist> {
    private val events: MutableList<Event<Playlist>> = mutableListOf()

    override fun save(event: Event<Playlist>) {
        // concurrency control by versioning - must be supported by the event store
        val existingRecords = getByAggregateId(event.aggregateId.toString())
        val latestVersion = existingRecords.maxByOrNull { it.version } ?.version ?: -1

        if (event.version == latestVersion + 1) {
            events.add(event)
        } else {
            throw RuntimeException("Wrong version - Cannot save event")
        }
    }

    override fun getByAggregateId(aggregateId: String): List<Event<Playlist>> {
        val id = UUID.fromString(aggregateId)
        return events.filter { it.aggregateId == id }
    }

    override fun getAll(): List<Event<Playlist>> = events.toMutableList().toList()
}