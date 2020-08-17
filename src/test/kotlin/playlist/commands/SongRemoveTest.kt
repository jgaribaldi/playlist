package playlist.commands

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import playlist.InMemoryEventRepository
import playlist.domain.Playlist
import java.lang.RuntimeException
import java.util.*

class SongRemoveTest {
    private lateinit var commandForAbsentSong: RemoveSong
    private lateinit var command: RemoveSong
    private val playlistOwner = "playlistOwner"
    private val playlistName = "playlistName"
    private val songName = "(de) Musica ligera"
    private val anotherSongName = "Signos"
    private val absentSongName = "Hombre al agua"
    private val playlistId = UUID.randomUUID().toString()
    private val inMemoryEventRepository = InMemoryEventRepository()

    @Test
    internal fun `should remove song from playlist`() {
        givenAnExistingPlaylistWithSongs()
        givenACommand()
        whenCommandIsExecuted()
        thenSongIsRemovedFromPlaylist()
    }

    @Test
    internal fun `should not remove song from non-existent playlist`() {
        givenACommand()
        assertThrows<RuntimeException>("Playlist does not exist") { whenCommandIsExecuted() }
    }

    @Test
    internal fun `should not remove song if not present in playlist`() {
        givenAnExistingPlaylistWithSongs()
        givenACommandForAbsentSong()
        assertThrows<RuntimeException>("Given song not present in given playlist") {
            whenCommandForAbsentSongIsExecuted()
        }
    }

    private fun whenCommandForAbsentSongIsExecuted() {
        val removeSongData = RemoveSongData(playlistId, absentSongName)
        commandForAbsentSong(removeSongData)
    }

    private fun givenACommandForAbsentSong() {
        commandForAbsentSong = RemoveSong(inMemoryEventRepository)
    }

    private fun givenAnExistingPlaylistWithSongs() {
        val createPlaylistData = CreatePlaylistData(playlistName, playlistOwner, playlistId)
        val createPlaylist = CreatePlaylist(inMemoryEventRepository)
        createPlaylist(createPlaylistData)
        val addSong = AddSong(inMemoryEventRepository)
        addSong(AddSongData(playlistId, songName))
        addSong(AddSongData(playlistId, anotherSongName))
    }

    private fun givenACommand() {
        command = RemoveSong(inMemoryEventRepository)
    }

    private fun whenCommandIsExecuted() {
        val removeSongData = RemoveSongData(playlistId, anotherSongName)
        command(removeSongData)
    }

    private fun thenSongIsRemovedFromPlaylist() {
        val sortedEvents = inMemoryEventRepository.getByAggregateId(playlistId)
        val playlist = Playlist.fromEvents(sortedEvents)!!
        assertThat(playlist.songList).doesNotContain(anotherSongName)
    }
}
