package pl.przemyslawpitus.mamyklocga

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class MamyKlocgaApplication

fun main(args: Array<String>) {
    runApplication<MamyKlocgaApplication>(*args)
}
