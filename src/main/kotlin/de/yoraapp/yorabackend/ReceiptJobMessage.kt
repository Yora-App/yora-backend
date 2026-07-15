package de.yoraapp.yorabackend

import org.springframework.data.annotation.Id
import java.util.UUID


data class ReceiptJobMessage(
    @Id val id: UUID? = null,
    val s3StorageID: String?
)

