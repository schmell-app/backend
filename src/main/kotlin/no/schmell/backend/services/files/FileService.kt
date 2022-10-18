package no.schmell.backend.services.files

import no.schmell.backend.dtos.files.FileDto
import org.springframework.web.multipart.MultipartFile

interface FileService {
    fun uploadFile(file: MultipartFile, gcpDirectoryName: String): FileDto
}