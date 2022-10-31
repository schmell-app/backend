package no.schmell.backend.services.files

import lombok.RequiredArgsConstructor
import mu.KLogging
import no.schmell.backend.dtos.files.FileDto
import no.schmell.backend.exceptions.BadRequestException
import no.schmell.backend.exceptions.GCPFileUploadException
import no.schmell.backend.lib.files.DataBucketUtil
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import javax.transaction.Transactional


@Service
@Transactional
@RequiredArgsConstructor
class FileServiceImpl(val dataBucketUtil: DataBucketUtil) : FileService {

    companion object: KLogging()

    override fun uploadFile(file: MultipartFile, directoryName: String): FileDto? {
        logger.debug("Start file uploading service")

        val fileDto: FileDto?
        val originalFileName: String = file.originalFilename
            ?: throw BadRequestException("Original file name is null")
        val path: Path = File(originalFileName).toPath()

        try {
            val contentType: String = Files.probeContentType(path)
            fileDto = dataBucketUtil.uploadFile(file, originalFileName, contentType, directoryName)
            if (fileDto != null) {
                logger.debug(
                    "File uploaded successfully, file name: {} and url: {}",
                    fileDto.fileName,
                    fileDto.fileUrl
                )
            }
        } catch (e: Exception) {
            logger.error("Error occurred while uploading. Error: ", e)
            throw GCPFileUploadException("Error occurred while uploading")
        }
        return fileDto
    }
}