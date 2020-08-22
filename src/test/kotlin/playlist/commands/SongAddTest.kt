package playlist.commands

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import playlist.InMemoryEventRepository
import playlist.domain.Event
import playlist.domain.Playlist
import java.lang.RuntimeException
import java.time.Instant
import java.util.*

class SongAddTest {
    private val playlistOwner = "playlistOwner"
    private val playlistName = "playlistName"
    private val songName = "(de) Musica ligera"
    private val playlistId = UUID.randomUUID().toString()
    private val inMemoryEventRepository = InMemoryEventRepository()
    private lateinit var command: AddSong

    @Test
    internal fun `should add song to playlist`() {
        givenAnExistingPlaylist()
        givenACommand()
        whenCommandIsExecuted()
        thenSongIsAddedToPlaylist()
    }

    @Test
    internal fun `should not add song to non-existent playlist`() {
        givenACommand()
        assertThrows<RuntimeException>("Playlist does not exist") { whenCommandIsExecuted() }
    }

    private fun givenAnExistingPlaylist() {
        val createPlaylistData = CreatePlaylistData(playlistId, playlistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun givenACommand() {
        command = AddSong(inMemoryEventRepository)
    }

    private fun whenCommandIsExecuted() {
        val addSongData = AddSongData(playlistId = playlistId, songName = songName)
        command(addSongData)
    }

    private fun thenSongIsAddedToPlaylist() {
        val sortedEvents = inMemoryEventRepository.getByAggregateId(playlistId)
        val playlist = Playlist.fromEvents(sortedEvents)!!
        assertThat(playlist.songList).contains(songName)
    }
}
