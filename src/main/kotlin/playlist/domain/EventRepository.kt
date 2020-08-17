package playlist.domain

interface EventRepository<T> {
    fun save(event: Event<T>)
    fun getByAggregateId(aggregateId: String): List<Event<T>>
}