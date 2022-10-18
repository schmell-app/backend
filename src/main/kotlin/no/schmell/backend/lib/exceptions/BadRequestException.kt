package no.schmell.backend.lib.exceptions

class BadRequestException(override val message: String) : RuntimeException(message)