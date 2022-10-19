package no.schmell.backend.dtos.files

data class FileDto(
    val fileName: String,
    val fileUrl: String,
    val isSuccess: Boolean,
    val statusCode: Int,
)