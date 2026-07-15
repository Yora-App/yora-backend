package de.yoraapp.yorabackend

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant
import java.util.UUID


enum class ReceiptStatus {
    UPLOADED,
    PROCESSING,
    OCR_COMPLETED,
    FAILED,
}

@Table("receipts")
data class Receipt(
    val status: ReceiptStatus,
    val contentType: String?,
    val filePath: String,
    val uploadedAt: Instant,
    val s3StorageId: String?,
    @Id val id: UUID? = null,
)