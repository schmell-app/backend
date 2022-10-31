package no.schmell.backend.exceptions

import lombok.Data

@Data
class BadRequestException(override val message: String) : RuntimeException(message)