package playlist.commands

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import playlist.InMemoryEventRepository
import playlist.domain.Playlist
import playlist.domain.PlaylistWasCreated
import java.util.*

class PlaylistCreateTest {

    private val eventRepository = InMemoryEventRepository()
    private lateinit var command: CreatePlaylist
    private val aggregateId = UUID.randomUUID().toString()
    private val playlistName = "playlistName"
    private val playlistOwner = "playlistOwner"

    @Test
    internal fun `should create playlist`() {
        givenACommand()
        whenCommandIsExecuted()
        thenPlaylistIsCreated()
    }

    private fun givenACommand() {
        command = CreatePlaylist(eventRepository)
    }

    private fun whenCommandIsExecuted() {
        command(CreatePlaylistData(playlistName, playlistOwner, aggregateId))
    }

    private fun thenPlaylistIsCreated() {
        val playlist = Playlist.fromEvents(eventRepository.getByAggregateId(aggregateId))!!
        assertThat(playlist.name).isEqualTo(playlistName)
        assertThat(playlist.owner).isEqualTo(playlistOwner)
        assertThat(playlist.songList).isEmpty()
    }
}
