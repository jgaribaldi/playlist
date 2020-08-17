package playlist.commands

abstract class Command<T> {
    abstract operator fun invoke(commandData: T)
}