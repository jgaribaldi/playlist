package playlist.queries

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import playlist.InMemoryEventRepository
import playlist.InMemoryProjectionRepository
import playlist.commands.CreatePlaylist
import playlist.commands.CreatePlaylistData
import playlist.commands.DeletePlaylist
import playlist.commands.DeletePlaylistData
import playlist.domain.Playlist
import java.util.*

class GetAllPlaylistsTest {
    private lateinit var result: List<Playlist>
    private lateinit var query: GetAllPlaylistsQuery
    private val anotherPlaylistId = UUID.randomUUID().toString()
    private val anotherPlaylistName = "anotherPlaylistName"
    private val playlistOwner = "playlistOwner"
    private val playlistName = "playlistName"
    private val playlistId = UUID.randomUUID().toString()
    private val inMemoryEventRepository = InMemoryEventRepository()
    private val inMemoryProjectionRepository = InMemoryProjectionRepository()

    init {
        inMemoryEventRepository.register(inMemoryProjectionRepository)
    }

    @Test
    internal fun `should get all the playlists`() {
        `given an existing playlist`()
        `given another existing playlist`()
        `given a query`()
        `when query is executed`()
        `then all playlists are returned`()
    }

    @Test
    internal fun `should get empty list if all playlists are deleted`() {
        `given a deleted playlist`()
        `given a query`()
        `when query is executed`()
        `then empty list is returned`()
    }

    private fun `given a deleted playlist`() {
        val createPlaylistData = CreatePlaylistData(playlistId, playlistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)

        val deletePlaylist = DeletePlaylist(inMemoryEventRepository)
        val deletePlaylistData = DeletePlaylistData(playlistId)
        deletePlaylist(deletePlaylistData)
    }

    private fun `then empty list is returned`() {
        assertThat(result).isEmpty()
    }

    private fun `given an existing playlist`() {
        val createPlaylistData = CreatePlaylistData(playlistId, playlistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun `given another existing playlist`() {
        val createPlaylistData = CreatePlaylistData(anotherPlaylistId, anotherPlaylistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun `given a query`() {
        query = GetAllPlaylistsQuery(inMemoryProjectionRepository)
    }

    private fun `when query is executed`() {
        result = query()
    }

    private fun `then all playlists are returned`() {
        assertThat(result.isNotEmpty())
        assertThat(result.map { it.id }).contains(playlistId, anotherPlaylistId)
    }
}

