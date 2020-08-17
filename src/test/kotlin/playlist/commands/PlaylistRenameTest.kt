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
        givenAnExistingPlaylist()
        givenACommand()
        whenCommandIsExecuted()
        thenPlaylistIsRenamed()
    }

    @Test
    internal fun `should not rename non-existent playlist`() {
        givenACommand()
        assertThrows<RuntimeException>("Playlist does not exist") { whenCommandIsExecuted() }
    }

    private fun givenAnExistingPlaylist() {
        val createPlaylistData = CreatePlaylistData(playlistName, playlistOwner, playlistId)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun givenACommand() {
        command = RenamePlaylist(inMemoryEventRepository)
    }

    private fun whenCommandIsExecuted() {
        val renamePlaylistData = RenamePlaylistData(id = playlistId, newName = playlistNewName)
        command(renamePlaylistData)
    }

    private fun thenPlaylistIsRenamed() {
        val playlist = Playlist.fromEvents(inMemoryEventRepository.getByAggregateId(playlistId))!!
        assertThat(playlist.name).isEqualTo(playlistNewName)
    }
}
