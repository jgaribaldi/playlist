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
        givenAnExistingPlaylist()
        givenAnotherExistingPlaylist()
        givenAQuery()
        whenQueryIsExecuted()
        thenAllPlaylistsAreReturned()
    }

    @Test
    internal fun `should get empty list if all playlists are deleted`() {
        givenADeletedPlaylist()
        givenAQuery()
        whenQueryIsExecuted()
        thenEmptyListIsReturned()
    }

    private fun givenADeletedPlaylist() {
        val createPlaylistData = CreatePlaylistData(playlistName, playlistOwner, playlistId)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)

        val deletePlaylist = DeletePlaylist(inMemoryEventRepository)
        val deletePlaylistData = DeletePlaylistData(playlistId)
        deletePlaylist(deletePlaylistData)
    }

    private fun thenEmptyListIsReturned() {
        assertThat(result).isEmpty()
    }

    private fun givenAnExistingPlaylist() {
        val createPlaylistData = CreatePlaylistData(playlistName, playlistOwner, playlistId)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun givenAnotherExistingPlaylist() {
        val createPlaylistData = CreatePlaylistData(anotherPlaylistName, playlistOwner, anotherPlaylistId)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun givenAQuery() {
        query = GetAllPlaylistsQuery(inMemoryProjectionRepository)
    }

    private fun whenQueryIsExecuted() {
        result = query()
    }

    private fun thenAllPlaylistsAreReturned() {
        assertThat(result.isNotEmpty())
        assertThat(result.map { it.id.toString() }).contains(playlistId, anotherPlaylistId)
    }
}

