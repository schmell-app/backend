package no.schmell.backend.services.files

import mu.KLogging
import no.schmell.backend.dtos.files.FileDto
import no.schmell.backend.lib.DataBucketUtil
import no.schmell.backend.lib.exceptions.BadRequestException
import no.schmell.backend.lib.exceptions.GCPFileUploadException
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.transaction.Transactional

@Service
@Transactional
class FileServiceImpl(val dataBucketUtil: DataBucketUtil) : FileService{

    companion object: KLogging()

    override fun uploadFile(file: MultipartFile, gcpDirectoryName: String): FileDto {
        val originalFileName = file.originalFilename ?: throw BadRequestException("Original file name is null")
        val path: Path = File(originalFileName).toPath()
        val fileDto: FileDto

        try {
            val contentType: String = Files.probeContentType(path)
            fileDto = dataBucketUtil.uploadFile(file, originalFileName, contentType, gcpDirectoryName)
            logger.info(
                "File uploaded successfully, file name: {} and url: {}",
                fileDto.fileName,
                fileDto.fileUrl
            )
        } catch (e: Exception) {
            logger.error("Error occurred while uploading. Error: ", e)
            throw GCPFileUploadException("Error occurred while uploading")
        }

        return fileDto
    }
}