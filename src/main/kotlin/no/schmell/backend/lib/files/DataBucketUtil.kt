package no.schmell.backend.lib.files

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.Blob
import com.google.cloud.storage.Bucket
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import com.google.gson.JsonObject
import mu.KLogging
import no.schmell.backend.dtos.files.FileDto
import no.schmell.backend.exceptions.BadRequestException
import no.schmell.backend.exceptions.FileWriteException
import no.schmell.backend.exceptions.GCPFileUploadException
import no.schmell.backend.exceptions.InvalidFileTypeException
import org.apache.commons.io.FileUtils
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import java.io.ByteArrayInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

@Component
class DataBucketUtil {

    companion object: KLogging()

    @Value("\${gcp.project.id}")
    lateinit var gcpProjectId: String

    @Value("\${gcp.bucket.id}")
    lateinit var gcpBucketId: String

    @Value("\${gcp.config.type}")
    lateinit var gcpConfigType: String

    @Value("\${gcp.config.project_id}")
    lateinit var gcpConfigProjectId: String

    @Value("\${gcp.config.private_key_id}")
    lateinit var gcpConfigPrivateKeyId: String

    @Value("\${gcp.config.private_key}")
    lateinit var gcpConfigPrivateKey: String

    @Value("\${gcp.config.client_email}")
    lateinit var gcpConfigClientEmail: String

    @Value("\${gcp.config.client_id}")
    lateinit var gcpConfigClientId: String

    @Value("\${gcp.config.auth_uri}")
    lateinit var gcpConfigAuthUri: String

    @Value("\${gcp.config.token_uri}")
    lateinit var gcpConfigTokenUri: String

    @Value("\${gcp.config.auth_provider_x509_cert_url}")
    lateinit var gcpConfigAuthProviderX509CertUrl: String

    @Value("\${gcp.config.client_x509_cert_url}")
    lateinit var gcpConfigClientX509CertUrl: String


    fun uploadFile(multipartFile: MultipartFile, fileName: String, contentType: String?, directoryName: String): FileDto? {
        val jsonObject = JsonObject()
        jsonObject.addProperty("type", gcpConfigType)
        jsonObject.addProperty("project_id", gcpConfigProjectId)
        jsonObject.addProperty("private_key_id", gcpConfigPrivateKeyId)
        jsonObject.addProperty("private_key", gcpConfigPrivateKey)
        jsonObject.addProperty("client_email", gcpConfigClientEmail)
        jsonObject.addProperty("client_id", gcpConfigClientId)
        jsonObject.addProperty("auth_uri", gcpConfigAuthUri)
        jsonObject.addProperty("token_uri", gcpConfigTokenUri)
        jsonObject.addProperty("auth_provider_x509_cert_url", gcpConfigAuthProviderX509CertUrl)
        jsonObject.addProperty("client_x509_cert_url", gcpConfigClientX509CertUrl)

        try {
            val convertedFile = convertFile(multipartFile)
            val fileData: ByteArray = FileUtils.readFileToByteArray(convertedFile)
            val inputStream: InputStream = ByteArrayInputStream(jsonObject.toString().toByteArray())
            val options: StorageOptions = StorageOptions.newBuilder().setProjectId(gcpProjectId)
                .setCredentials(GoogleCredentials.fromStream(inputStream)).build()
            val storage: Storage = options.service
            val bucket: Bucket = storage.get(gcpBucketId, Storage.BucketGetOption.fields())
            checkFileExtension(fileName)
            val blob: Blob = bucket.create(
                "$directoryName/$fileName",
                fileData,
                contentType
            )
            logger.info { "File successfully uploaded to GCS" }

            // Delete file after upload
            convertedFile?.delete()
            return FileDto(blob.name, blob.mediaLink)
        } catch (e: Exception) {
            logger.error { "An error occurred while uploading data. Exception: $e"  }
            throw GCPFileUploadException("An error occurred while storing data to GCS")
        }
    }

    private fun convertFile(file: MultipartFile): File? {
         try {
            if (file.originalFilename == null) {
                throw BadRequestException("Original file name is null")
            }
            val convertedFile = File(file.originalFilename)
            val outputStream = FileOutputStream(convertedFile)
            outputStream.write(file.bytes)
            outputStream.close()
            logger.debug("Converting multipart file : {}", convertedFile)
            return convertedFile
        } catch (e: Exception) {
            throw FileWriteException("An error has occurred while converting the file")
        }
    }

    private fun checkFileExtension(fileName: String?): String {
        if (fileName != null && fileName.contains(".")) {
            val extensionList = arrayOf(".png", ".jpeg", ".jpg")
            for (extension in extensionList) {
                if (fileName.endsWith(extension)) {
                    logger.debug("Accepted file type : {}", extension)
                    return extension
                }
            }
        }
        logger.error("Not a permitted file type")
        throw InvalidFileTypeException("Not a permitted file type")
    }
}