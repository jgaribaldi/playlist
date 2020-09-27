package playlist.queries

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import playlist.InMemoryEventRepository
import playlist.InMemoryProjectionRepository
import playlist.commands.CreatePlaylist
import playlist.commands.CreatePlaylistData
import playlist.domain.Playlist
import java.util.*

class GetPlaylistByIdTest {
    private var result: Playlist? = null
    private lateinit var query: GetPlaylistByIdQuery
    private val playlistOwner = "playlistOwner"
    private val playlistName = "playlistName"
    private val playlistId = UUID.randomUUID().toString()
    private val inMemoryEventRepository = InMemoryEventRepository()
    private val inMemoryProjectionRepository = InMemoryProjectionRepository()

    init {
        inMemoryEventRepository.register(inMemoryProjectionRepository)
    }

    @Test
    internal fun `should get playlist by id`() {
        `given an existing playlist`()
        `given a query`()
        `when query is executed`()
        `then playlist is obtained`()
    }

    @Test
    internal fun `should get null if playlist does not exist`() {
        `given a query`()
        `when query is executed`()
        `then null result is obtained`()
    }

    private fun `then null result is obtained`() {
        assertThat(result).isNull()
    }

    private fun `given an existing playlist`() {
        val createPlaylistData = CreatePlaylistData(playlistId, playlistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun `given a query`() {
        query = GetPlaylistByIdQuery(inMemoryProjectionRepository)
    }

    private fun `when query is executed`() {
        result = query(playlistId)
    }

    private fun `then playlist is obtained`() {
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(playlistId)
        assertThat(result?.name).isEqualTo(playlistName)
        assertThat(result?.owner).isEqualTo(playlistOwner)
    }
}

