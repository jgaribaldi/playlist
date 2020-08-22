package playlist.domain

interface ProjectionRepository<T> {
    fun getById(aggregateId: String): T?
    fun getAll(): List<T>
}