package no.schmell.backend.exceptions

import lombok.Data

@Data
class FileWriteException(override val message: String) : RuntimeException(message)