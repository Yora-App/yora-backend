package de.yoraapp.yorabackend.receipt

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.time.Instant
import java.util.UUID

@Service
class ReceiptService(private val db: ReceiptRepository, private val receiptJobPublisher: ReceiptJobPublisher) {
    fun createReceipt(file: MultipartFile): Receipt {

        // Create directory to store uploads
        val uploadDir = Paths.get("./uploads")
        Files.createDirectories(uploadDir)

        // Create path where to store the uploaded file
        val destinationPath = uploadDir.resolve(file.originalFilename.toString())

        // Store uploaded file at the destination
        file.transferTo(destinationPath)

        val receipt = Receipt(
            status = ReceiptStatus.UPLOADED,
            contentType = file.contentType,
            filePath = destinationPath.toString(),
            uploadedAt = Instant.now(),
            s3StorageId = "placeholder",
        )
        val savedReceipt = db.save(receipt)

        receiptJobPublisher.publish(savedReceipt)

        return savedReceipt
    }

    fun findAllReceipts(): List<Receipt> = db.findAll().toList()

    fun findReceiptByID(id: UUID): Receipt? = db.findByIdOrNull(id)
}