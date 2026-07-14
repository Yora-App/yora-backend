package de.yoraapp.yorabackend

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import java.net.URI

@ConfigurationProperties(prefix = "garage")
data class GarageProperties(
    val accessKey: String,
    val secretKey: String,
    val region: String,
    val endpoint: String,
    val bucket: String,
)

@Configuration
class S3Config(private val garageProperties: GarageProperties) {
    @Bean
    fun s3Client(): S3Client{
        return S3Client.builder()
            .endpointOverride(URI.create(garageProperties.endpoint))
            .region(Region.of(garageProperties.region))
            .credentialsProvider(
                StaticCredentialsProvider.create(
                    AwsBasicCredentials.create(garageProperties.accessKey, garageProperties.secretKey)
                )
            )
            .forcePathStyle(true)
            .build()
    }
}
