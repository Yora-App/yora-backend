package de.yoraapp.yorabackend.messaging

import de.yoraapp.yorabackend.receipt.Receipt
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class ReceiptJobPublisher(private val rabbitTemplate: RabbitTemplate) {
    fun publish(receipt: Receipt) {

        val messageToSend = ReceiptJobMessage(receipt.id, receipt.s3StorageId)
        rabbitTemplate.convertAndSend("ocr_jobs_queue", messageToSend)
    }
}