package de.yoraapp.yorabackend

import de.yoraapp.yorabackend.messaging.ReceiptJobPublisher
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.servlet.client.RestTestClient
import kotlin.random.Random
import io.mockk.*

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YoraBackendApplicationTests {

    @LocalServerPort
    private var port: Int = 0

    private lateinit var client: RestTestClient

    val receiptJobPublisherMock = mockk<ReceiptJobPublisher>()

    @BeforeEach
    fun setup() {
        client = RestTestClient.bindToServer().baseUrl("http://localhost:$port").build()
    }

    @Test
    fun contextLoads() {
    }

    @Test
    fun receiptUploadTest() {
        every { receiptJobPublisherMock.publish(any()) } just Runs

        val randomBinaryData = ByteArray(1024).also { Random.nextBytes(it) } //1024 random bytes

        val multipart = MultipartBodyBuilder()
        multipart.part(
            "uploadFile",
            randomBinaryData
        ).filename("test.jpg")
        .contentType(MediaType.IMAGE_JPEG)

        client.post().uri("/receipt")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(multipart.build())
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody()

        //TODO: check that returned JSON body has correct format and content

        //TODO: check whether receipt was written into database

        //check whether rabbitmq publish method was called exactly once
        verify(exactly = 1) {
            receiptJobPublisherMock.publish(any())
        }
    }
}
