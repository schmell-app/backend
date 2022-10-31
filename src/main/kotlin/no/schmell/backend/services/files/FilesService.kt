package no.schmell.backend.services.files

import com.amazonaws.AmazonServiceException
import com.amazonaws.HttpMethod
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.model.PutObjectRequest
import mu.KLogging
import no.schmell.backend.dtos.files.FileDto
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.nio.file.Files
import java.util.*


@Service
class FilesService(val amazonS3Client: AmazonS3) {

    companion object: KLogging()

    private fun convertFromMultipartToFile(multipartFile: MultipartFile): File {
        val file = File(multipartFile.getOriginalFilename())
        try {
            FileOutputStream(file).use { outputStream -> outputStream.write(multipartFile.bytes) }
        } catch (e: IOException) {
            logger.error("Error {} occurred while converting the multipart file", e.localizedMessage)
        }
        return file
    }

    fun saveFile(multipartFile: MultipartFile, s3BucketName: String, directoryName: String): FileDto? {
        var fileDto: FileDto? = null
        try {
            val file: File = convertFromMultipartToFile(multipartFile)
            logger.info { "Uploading file with name {${file.name}}" }
            val putObjectRequest = PutObjectRequest(s3BucketName, "$directoryName/${file.name}", file)
            amazonS3Client.putObject(putObjectRequest)
            Files.delete(file.toPath())
            fileDto = FileDto(
                "$directoryName/${file.name}",
                amazonS3Client.getUrl(s3BucketName, "$directoryName/${file.name}").toString(),
            )
        } catch (e: AmazonServiceException) {
            logger.error("Error {} occurred while uploading file", e.localizedMessage)
        } catch (ex: IOException) {
            logger.error("Error {} occurred while deleting temporary file", ex.localizedMessage)
        }
        return fileDto
    }

    fun generatePresignedUrl(s3BucketName: String, fileName: String): String {
        val calendar = Calendar.getInstance()
        calendar.time = Date()
        calendar.add(Calendar.DATE, 1)

        return amazonS3Client.generatePresignedUrl(s3BucketName, fileName, calendar.time, HttpMethod.GET).toString()
    }
}