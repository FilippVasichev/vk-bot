package com.vkBot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class VkBotApplication

fun main(args: Array<String>) {
    runApplication<VkBotApplication>(*args)
}
