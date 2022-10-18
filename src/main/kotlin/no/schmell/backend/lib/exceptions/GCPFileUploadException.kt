package no.schmell.backend.lib.exceptions

data class GCPFileUploadException(override val message: String) : RuntimeException(message)