package no.schmell.backend.lib.files

import com.google.auth.oauth2.GoogleCredentials
import com.google.cloud.storage.*
import com.google.gson.JsonObject
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.io.ByteArrayInputStream
import java.io.InputStream
import java.net.URL
import java.util.concurrent.TimeUnit

@Component
class GenerateObjectSignedUrl {

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

    fun generateSignedUrl(objectName: String?): URL {
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

        val inputStream: InputStream = ByteArrayInputStream(jsonObject.toString().toByteArray())
        val storage: Storage = StorageOptions.newBuilder().setProjectId(gcpProjectId).setCredentials(
            GoogleCredentials.fromStream(inputStream)).build().service

        val blobInfo = BlobInfo.newBuilder(BlobId.of(gcpBucketId, objectName)).build()

        return storage.signUrl(blobInfo, 24, TimeUnit.HOURS, Storage.SignUrlOption.withV4Signature())
    }
}