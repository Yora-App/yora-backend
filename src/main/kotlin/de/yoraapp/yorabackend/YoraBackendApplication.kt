package de.yoraapp.yorabackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class YoraBackendApplication

fun main(args: Array<String>) {
    runApplication<YoraBackendApplication>(*args)
}
