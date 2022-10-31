package no.schmell.backend.exceptions

import lombok.Data

@Data
class InvalidFileTypeException(override val message: String) : RuntimeException(message)