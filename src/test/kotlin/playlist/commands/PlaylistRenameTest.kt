package playlist.commands

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import playlist.InMemoryEventRepository
import playlist.domain.Playlist
import java.util.*

class PlaylistRenameTest {
    private val playlistOwner = "playlistOwner"
    private val playlistName = "playlistName"
    private val playlistNewName = "playlistNewName"
    private val playlistId = UUID.randomUUID().toString()
    private lateinit var command: RenamePlaylist
    private val inMemoryEventRepository = InMemoryEventRepository()

    @Test
    internal fun `should rename playlist`() {
        `given an existing playlist`()
        `given a command`()
        `when command is executed`()
        `then playlist is renamed`()
    }

    @Test
    internal fun `should not rename non-existent playlist`() {
        `given a command`()
        assertThrows<RuntimeException>("Playlist does not exist") { `when command is executed`() }
    }

    private fun `given an existing playlist`() {
        val createPlaylistData = CreatePlaylistData(playlistId, playlistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun `given a command`() {
        command = RenamePlaylist(inMemoryEventRepository)
    }

    private fun `when command is executed`() {
        val renamePlaylistData = RenamePlaylistData(id = playlistId, newName = playlistNewName)
        command(renamePlaylistData)
    }

    private fun `then playlist is renamed`() {
        val playlist = Playlist.fromEvents(inMemoryEventRepository.getByAggregateId(playlistId))!!
        assertThat(playlist.name).isEqualTo(playlistNewName)
    }
}
