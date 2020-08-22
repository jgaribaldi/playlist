package playlist.queries

import playlist.domain.Playlist
import playlist.domain.ProjectionRepository

class GetPlaylistByIdQuery(
    private val projectionRepository: ProjectionRepository<Playlist>
) {
    operator fun invoke(playlistId: String): Playlist? = projectionRepository.getById(playlistId)
}