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
        givenAnExistingPlaylist()
        givenAQuery()
        whenQueryIsExecuted()
        thenPlaylistIsGot()
    }

    @Test
    internal fun `should get null if playlist does not exist`() {
        givenAQuery()
        whenQueryIsExecuted()
        thenNullResultIsGot()
    }

    private fun thenNullResultIsGot() {
        assertThat(result).isNull()
    }

    private fun givenAnExistingPlaylist() {
        val createPlaylistData = CreatePlaylistData(playlistName, playlistOwner, playlistId)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun givenAQuery() {
        query = GetPlaylistByIdQuery(inMemoryProjectionRepository)
    }

    private fun whenQueryIsExecuted() {
        result = query(playlistId)
    }

    private fun thenPlaylistIsGot() {
        assertThat(result).isNotNull
        assertThat(result?.id).isEqualTo(UUID.fromString(playlistId)!!)
        assertThat(result?.name).isEqualTo(playlistName)
        assertThat(result?.owner).isEqualTo(playlistOwner)
    }
}

