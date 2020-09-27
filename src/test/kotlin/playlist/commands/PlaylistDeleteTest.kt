package playlist.commands

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import playlist.InMemoryEventRepository
import playlist.domain.Playlist
import java.lang.RuntimeException
import java.util.*

class PlaylistDeleteTest {
    private lateinit var command: DeletePlaylist
    private val playlistId = UUID.randomUUID().toString()
    private val playlistName = "playlistName"
    private val playlistOwner = "playlistOwner"
    private val inMemoryEventRepository = InMemoryEventRepository()

    @Test
    internal fun `should delete playlist`() {
        `given an existing playlist`()
        `given a command`()
        `when command is executed`()
        `then playlist is deleted`()
    }

    @Test
    internal fun `should not delete non-existent playlist`() {
        `given a command`()
        assertThrows<RuntimeException>("Playlist does not exist") { `when command is executed`() }
    }

    private fun `given an existing playlist`() {
        val createPlaylistData = CreatePlaylistData(playlistId, playlistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun `given a command`() {
        command = DeletePlaylist(inMemoryEventRepository)
    }

    private fun `when command is executed`() {
        val deletePlaylistData = DeletePlaylistData(playlistId)
        command(deletePlaylistData)
    }

    private fun `then playlist is deleted`() {
        val playlist = Playlist.fromEvents(inMemoryEventRepository.getByAggregateId(playlistId))
        assertThat(playlist).isNull()
    }
}
