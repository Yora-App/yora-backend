package de.yoraapp.yorabackend.message

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
class MessageController(private val messageService: MessageService) {
    @GetMapping("/")
    fun index(@RequestParam name: String) = "Hello, $name!"

    @GetMapping("/messages")
    fun listMessages() = messageService.findMessages()

    @PostMapping("/add_message")
    fun addMessage(@RequestBody message: Message) = messageService.save(message)
}