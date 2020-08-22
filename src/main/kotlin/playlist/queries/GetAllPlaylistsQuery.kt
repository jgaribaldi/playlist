package playlist.queries

import playlist.domain.Playlist
import playlist.domain.ProjectionRepository

class GetAllPlaylistsQuery(
    private val projectionRepository: ProjectionRepository<Playlist>
) {
    operator fun invoke(): List<Playlist> = projectionRepository.getAll()
}