package playlist.commands

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import playlist.InMemoryEventRepository
import playlist.domain.Playlist
import java.util.*

class PlaylistCreateTest {

    private val eventRepository = InMemoryEventRepository()
    private lateinit var command: CreatePlaylist
    private val playlistId = UUID.randomUUID().toString()
    private val playlistName = "playlistName"
    private val playlistOwner = "playlistOwner"

    @Test
    internal fun `should create playlist`() {
        `given a command`()
        `when command is executed`()
        `then playlist is created`()
    }

    private fun `given a command`() {
        command = CreatePlaylist(eventRepository)
    }

    private fun `when command is executed`() {
        command(CreatePlaylistData(playlistId, playlistName, playlistOwner))
    }

    private fun `then playlist is created`() {
        val playlist = Playlist.fromEvents(eventRepository.getByAggregateId(playlistId))!!
        assertThat(playlist.name).isEqualTo(playlistName)
        assertThat(playlist.owner).isEqualTo(playlistOwner)
        assertThat(playlist.songList).isEmpty()
    }
}
