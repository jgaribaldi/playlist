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
    private val aggregateId = UUID.randomUUID().toString()
    private val playlistName = "playlistName"
    private val playlistOwner = "playlistOwner"
    private val inMemoryEventRepository = InMemoryEventRepository()

    @Test
    internal fun `should delete playlist`() {
        givenAnExistingPlaylist()
        givenACommand()
        whenCommandIsExecuted()
        thenPlaylistIsDeleted()
    }

    @Test
    internal fun `should not delete non-existent playlist`() {
        givenACommand()
        assertThrows<RuntimeException>("Playlist does not exist") { whenCommandIsExecuted() }
    }

    private fun givenAnExistingPlaylist() {
        val createPlaylistData = CreatePlaylistData(playlistName, playlistOwner, aggregateId)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun givenACommand() {
        command = DeletePlaylist(inMemoryEventRepository)
    }

    private fun whenCommandIsExecuted() {
        val deletePlaylistData = DeletePlaylistData(aggregateId)
        command(deletePlaylistData)
    }

    private fun thenPlaylistIsDeleted() {
        val playlist = Playlist.fromEvents(inMemoryEventRepository.getByAggregateId(aggregateId))
        assertThat(playlist).isNull()
    }
}
