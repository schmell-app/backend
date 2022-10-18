package no.schmell.backend.lib.exceptions

data class FileWriteException(override val message: String) : RuntimeException(message)