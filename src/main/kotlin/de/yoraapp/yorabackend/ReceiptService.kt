package de.yoraapp.yorabackend

import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Service
class ReceiptService(private val db: ReceiptRepository) {
    fun createReceipt(file: MultipartFile): Receipt{
        val id = UUID.randomUUID()
        val contentType = file.contentType

        // Create directory to store uploads
        val uploadDir = Paths.get("./uploads")
        Files.createDirectories(uploadDir)

        // Extract file extension
        val fileExtension = file.originalFilename
            ?.substringAfterLast(".", "")
            ?.takeIf { it.isNotEmpty() }
            ?.let { ".$it" }
            ?: ""

        val filename = id.toString() + fileExtension
        val destinationPath = uploadDir.resolve(filename)

        file.transferTo(destinationPath)

        val receipt = Receipt(
            status = ReceiptStatus.UPLOADED,
            contentType = contentType,
            id = id,
            filePath = destinationPath.toString()

        )

        return db.save(receipt)
    }

    fun findAllReceipts(): List<Receipt> = db.findAll().toList()

    fun findReceiptByID(id: UUID): Receipt? = db.findByIdOrNull(id)
}