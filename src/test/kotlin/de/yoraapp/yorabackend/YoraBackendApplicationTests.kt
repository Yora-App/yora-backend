package de.yoraapp.yorabackend

import com.ninjasquad.springmockk.MockkBean
import de.yoraapp.yorabackend.messaging.ReceiptJobPublisher
import de.yoraapp.yorabackend.receipt.Receipt
import de.yoraapp.yorabackend.receipt.ReceiptRepository
import de.yoraapp.yorabackend.receipt.ReceiptStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.http.MediaType
import org.springframework.http.client.MultipartBodyBuilder
import org.springframework.test.web.servlet.client.RestTestClient
import kotlin.random.Random
import io.mockk.*
import org.assertj.core.api.Assertions.assertThat
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.web.servlet.client.expectBody
import org.springframework.web.bind.annotation.ResponseBody
import java.time.Instant
import java.time.temporal.ChronoUnit

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class YoraBackendApplicationTests{
    @LocalServerPort
    private var port: Int = 0

    private lateinit var client: RestTestClient

    @Autowired
    private lateinit var receiptRepository: ReceiptRepository

    @MockkBean
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

        val returnedReceipt: Receipt? = client.post().uri("/receipt")
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .body(multipart.build())
            .exchange()
            .expectStatus().isCreated()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<Receipt>()
            .returnResult().getResponseBody()

        //check whether returned JSON body has correct format and content
        assertThat(returnedReceipt!!.status).isEqualTo(ReceiptStatus.UPLOADED)
        assertThat(returnedReceipt.contentType).isEqualTo("image/jpeg")

        //check whether receipt was written into database
        assertThat(receiptRepository.count()).isEqualTo(1)
        val dbReceipt = receiptRepository.findAll().first()
        assertThat(dbReceipt.id).isEqualTo(returnedReceipt.id)

        //check whether rabbitmq publish method was called exactly once with the right receipt object
        verify(exactly = 1) {
            receiptJobPublisherMock.publish(dbReceipt)
        }
    }

    @Test
    fun getReceiptsTest(){

        val receipt = Receipt(
            status = ReceiptStatus.UPLOADED,
            contentType = MediaType.IMAGE_JPEG.toString(),
            filePath = "test_path",
            uploadedAt = Instant.now().truncatedTo(ChronoUnit.MICROS), //PostgreSQL and H2 both only have microsecond precision
            s3StorageId = "placeholder",
        )
        receiptRepository.save(receipt)

        val allReceipts : List<Receipt>? = client.get().uri("/receipts")
            .exchange()
            .expectStatus().isOk()
            .expectHeader().contentType(MediaType.APPLICATION_JSON)
            .expectBody<List<Receipt>>()
            .returnResult().getResponseBody()

        // check that allReceipts contains one receipt
        assertThat(allReceipts!!.size).isEqualTo(1)

        // Check that the values of the returned receipt are equal to those of the receipt in the database
        assertThat(allReceipts.first().status).isEqualTo(ReceiptStatus.UPLOADED)
        assertThat(allReceipts.first().contentType).isEqualTo(MediaType.IMAGE_JPEG.toString())
        assertThat(allReceipts.first().filePath).isEqualTo("test_path")
        assertThat(allReceipts.first().s3StorageId).isEqualTo("placeholder")
    }
}
