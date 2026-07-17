package de.yoraapp.yorabackend.receipt

import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile
import java.util.UUID

@RestController
class ReceiptController(private val receiptService: ReceiptService) {
    @PostMapping("/receipt", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadFile(@RequestParam uploadFile: MultipartFile): ResponseEntity<Any>{

        if(uploadFile.isEmpty){
            return ResponseEntity.badRequest().build()
        }
        // TODO: Move file validation to ReceiptService when another upload entry point exists
        if(uploadFile.contentType !in listOf("image/jpeg", "image/png", "application/pdf")) {
            return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE).build()
        }

        val receipt = receiptService.createReceipt(uploadFile)
        return ResponseEntity.status(HttpStatus.CREATED).body(receipt)
    }

    @GetMapping("/receipts")
    fun getReceipts() = receiptService.findAllReceipts()

    @GetMapping("/receipt/{id}")
    fun getReceiptById(@PathVariable id: UUID) = receiptService.findReceiptByID(id)
}