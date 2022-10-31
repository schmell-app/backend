package no.schmell.backend.exceptions

import lombok.Data

@Data
class GCPFileUploadException(override val message: String) : RuntimeException(message)