package playlist

import playlist.domain.Event
import playlist.domain.Playlist
import playlist.domain.ProjectionRepository
import playlist.infrastructure.Observer
import java.util.*
import kotlin.collections.HashMap

class InMemoryProjectionRepository: ProjectionRepository<Playlist>, Observer<Event<Playlist>> {

    private val aggregateMap: HashMap<UUID, Playlist> = hashMapOf()

    override fun getById(aggregateId: String): Playlist? {
        val id = UUID.fromString(aggregateId)!!
        return aggregateMap[id]?.copy()
    }

    override fun getAll(): List<Playlist> = aggregateMap.values.toList()

    override fun update(newValue: Event<Playlist>) {
        val playlistId = newValue.aggregateId
        val oldPlaylist = aggregateMap[playlistId]
        val newPlaylist = newValue.apply(oldPlaylist)

        if (newPlaylist == null) {
            // applied event resulted in playlist being deleted
            aggregateMap.remove(playlistId)
        } else {
            aggregateMap[playlistId] = newPlaylist
        }
    }
}
