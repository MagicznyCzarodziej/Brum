package pl.przemyslawpitus.brum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration
import org.springframework.boot.runApplication

@SpringBootApplication(exclude = [SecurityAutoConfiguration::class])
class BrumApplication

fun main(args: Array<String>) {
    runApplication<BrumApplication>(*args)
}
