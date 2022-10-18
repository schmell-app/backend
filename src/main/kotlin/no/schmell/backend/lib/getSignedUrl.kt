package no.schmell.backend.lib

import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.storage.BlobId
import com.google.cloud.storage.BlobInfo
import com.google.cloud.storage.Storage
import com.google.cloud.storage.StorageOptions
import org.springframework.core.io.ClassPathResource
import java.io.File
import java.io.FileInputStream
import java.net.URL
import java.util.concurrent.TimeUnit

fun getSignedUrl(
    gcpProjectId: String,
    gcpBucketId: String,
    gcpConfigFile: String,
    logoUrl: String?): URL? {
    return if (logoUrl != null) {
        val storage: Storage = StorageOptions.newBuilder().setProjectId(gcpProjectId).build().service

        // Define resource
        val blobInfo: BlobInfo = BlobInfo.newBuilder(BlobId.of(gcpBucketId, logoUrl)).build()

        val resource = ClassPathResource(gcpConfigFile).uri
        val file = File(resource)
        return storage.signUrl(
            blobInfo, 24, TimeUnit.HOURS, Storage.SignUrlOption.signWith(
                ServiceAccountCredentials.fromStream(
                    FileInputStream(file)
                )
            )
        )
    } else {
        return null
    }

}