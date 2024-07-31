package com.vkBot.client.config

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("vk")
data class VkBotProperties(
    val callbackVerificationCode: String,
    val groupId: Int,
    val groupApiKey: String,
    val apiVersion: Double,
)
