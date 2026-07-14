package de.yoraapp.yorabackend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(GarageProperties::class)
class YoraBackendApplication

fun main(args: Array<String>) {
    runApplication<YoraBackendApplication>(*args)
}
