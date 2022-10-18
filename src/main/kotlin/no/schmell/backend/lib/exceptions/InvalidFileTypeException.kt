package no.schmell.backend.lib.exceptions

data class InvalidFileTypeException(override val message: String) : RuntimeException(message)