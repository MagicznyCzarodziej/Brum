package pl.przemyslawpitus.brum

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BrumApplication

fun main(args: Array<String>) {
    runApplication<BrumApplication>(*args)
}
