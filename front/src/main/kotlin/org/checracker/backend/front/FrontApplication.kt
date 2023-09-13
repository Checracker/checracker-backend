package org.checracker.backend.front

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.boot.runApplication
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@EntityScan(basePackages = ["org.checracker.backend.core"])
@EnableJpaRepositories(basePackages = ["org.checracker.backend.core"])
@SpringBootApplication
class FrontApplication

fun main(args: Array<String>) {
    runApplication<FrontApplication>(*args)
}
