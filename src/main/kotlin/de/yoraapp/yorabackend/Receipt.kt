package de.yoraapp.yorabackend

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
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
    @Id val id: UUID,

)