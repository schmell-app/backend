package no.schmell.backend.lib

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import mu.KLogging
import net.bytebuddy.utility.RandomString
import no.schmell.backend.dtos.files.FileDto
import no.schmell.backend.lib.exceptions.BadRequestException
import no.schmell.backend.lib.exceptions.FileWriteException
import no.schmell.backend.lib.exceptions.GCPFileUploadException
import no.schmell.backend.lib.exceptions.InvalidFileTypeException
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.util.concurrent.ThreadLocalRandom
import org.apache.commons.io.FileUtils

@Component
class DataBucketUtil {
    companion object: KLogging()

    @Value("\${gcp.config.file}")
    lateinit var gcpConfigFile: String

    @Value("\${gcp.project.id}")
    lateinit var gcpProjectId: String

    @Value("\${gcp.bucket.id}")
    lateinit var gcpBucketId: String

    fun uploadFile(multipartFile: MultipartFile, fileName: String, contentType: String, gcpDirectoryName: String): FileDto {
        try {
            logger.info("Start file uploading process on GCS")
            val fileData: ByteArray = FileUtils.readFileToByteArray(convertFile(multipartFile))
            val inputStream: InputStream = ClassPathResource(gcpConfigFile).inputStream
            val options: StorageOptions = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                .setCredentials(GoogleCredentials.fromStream(inputStream)).build()
            val storage: Storage = options.service
            val bucket: Bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields())
            val id = RandomString(6, ThreadLocalRandom.current())
            val blob: Blob = bucket.create(
                (gcpDirectoryName + "/" + fileName + "-" + id.nextString()) + checkFileExtension(fileName),
                fileData,
                contentType
            )
            logger.info("File successfully uploaded to GCS")
            return FileDto(blob.name, blob.mediaLink)
        } catch (e: Exception) {
            logger.error("An error occurred while uploading data. Exception: ", e)
            throw GCPFileUploadException("An error occurred while storing data to GCS")
        }
    }

    private fun convertFile(file: MultipartFile): File {
        return try {
            if (file.originalFilename == null) {
                throw BadRequestException("Original file name is null")
            }
            val convertedFile = File(file.originalFilename!!)
            val outputStream = FileOutputStream(convertedFile)
            outputStream.write(file.bytes)
            outputStream.close()
            logger.debug("Converting multipart file : {}", convertedFile)
            convertedFile
        } catch (e: Exception) {
            throw FileWriteException("An error has occurred while converting the file")
        }
    }

    private fun checkFileExtension(fileName: String?): String {
        if (fileName != null && fileName.contains(".")) {
            val extensionList = arrayOf(".png", ".jpeg")
            for (extension in extensionList) {
                if (fileName.endsWith(extension)) {
                    logger.info("Accepted file type : {}", extension)
                    return extension
                }
            }
        }
        logger.error("Not a permitted file type")
        throw InvalidFileTypeException("Not a permitted file type")
    }
}