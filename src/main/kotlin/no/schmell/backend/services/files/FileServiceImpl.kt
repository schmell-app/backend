package no.schmell.backend.services.files

import mu.KLogging
import no.schmell.backend.dtos.files.FileDto
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.GetUrlRequest
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.util.*
import javax.transaction.Transactional

@Service
@Transactional
class FileServiceImpl(var s3Client: S3Client) : FileService{

    companion object: KLogging()

    @Value("\$aws.bucketName")
    lateinit var bucketName: String

    override fun uploadFile(file: MultipartFile, directoryName: String): FileDto {
        val originFileName: String? = file.originalFilename
        val key = "$directoryName/$originFileName"

        val response = s3Client.putObject(
                PutObjectRequest.builder().bucket(bucketName).key(key).build(),
                RequestBody.fromBytes(file.bytes))
        val request: GetUrlRequest = GetUrlRequest.builder().bucket(bucketName).key(key).build()
        val url: String = s3Client.utilities().getUrl(request).toExternalForm()

        return FileDto(url, key, response.sdkHttpResponse().isSuccessful, response.sdkHttpResponse().statusCode())
    }
}