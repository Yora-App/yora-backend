package de.yoraapp.yorabackend

import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.awt.PageAttributes
import java.util.UUID

@RestController
class ReceiptController(private val receiptService: ReceiptService) {
    @PostMapping("/receipt", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@RequestParam uploadFile: MultipartFile) = receiptService.createReceipt(uploadFile)

    @GetMapping("/receipts")
    fun getReceipts() = receiptService.findAllReceipts()

    @GetMapping("/receipt/{id}")
    fun getReceiptById(@PathVariable id: UUID) = receiptService.findReceiptByID(id)
}