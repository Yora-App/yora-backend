package de.yoraapp.yorabackend

import com.rabbitmq.client.AMQP
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Service
class ReceiptService(private val db: ReceiptRepository, private val rabbitTemplate: RabbitTemplate) {
    fun createReceipt(file: MultipartFile): Receipt{

        val contentType = file.contentType

        // Create directory to store uploads
        val uploadDir = Paths.get("./uploads")
        Files.createDirectories(uploadDir)

        // Create path where to store the uploaded file
        val destinationPath = uploadDir.resolve(file.originalFilename.toString())

        // Store uploaded file at the destination
        file.transferTo(destinationPath)

        val receipt = Receipt(
            status = ReceiptStatus.UPLOADED,
            contentType = contentType,
            filePath = destinationPath.toString(),
        )
        val savedReceipt = db.save(receipt)

        rabbitTemplate.convertAndSend("orc_jobs_queue", savedReceipt)
        return savedReceipt
    }

    fun findAllReceipts(): List<Receipt> = db.findAll().toList()

    fun findReceiptByID(id: UUID): Receipt? = db.findByIdOrNull(id)
}