package playlist.commands

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import playlist.InMemoryEventRepository
import playlist.domain.Playlist
import java.lang.RuntimeException
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
        `given an existing playlist`()
        `given a command`()
        `when command is executed`()
        `then song is added to playlist`()
    }

    @Test
    internal fun `should not add song to non-existent playlist`() {
        `given a command`()
        assertThrows<RuntimeException>("Playlist does not exist") { `when command is executed`() }
    }

    private fun `given an existing playlist`() {
        val createPlaylistData = CreatePlaylistData(playlistId, playlistName, playlistOwner)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
    }

    private fun `given a command`() {
        command = AddSong(inMemoryEventRepository)
    }

    private fun `when command is executed`() {
        val addSongData = AddSongData(playlistId = playlistId, songName = songName)
        command(addSongData)
    }

    private fun `then song is added to playlist`() {
        val sortedEvents = inMemoryEventRepository.getByAggregateId(playlistId)
        val playlist = Playlist.fromEvents(sortedEvents)!!
        assertThat(playlist.songList).contains(songName)
    }
}
