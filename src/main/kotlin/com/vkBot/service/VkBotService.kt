package com.vkBot.service

import com.vkBot.client.VkBotApi
import com.vkBot.client.config.VkBotProperties
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class VkBotService(
    private val vkBotApi: VkBotApi,
    private val vkBotProperties: VkBotProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun reply(
        message: String,
        userId: Int,
        messageId: Int,
    ) {
        runCatching {
            vkBotApi.sendMessage(
                groupApiKey = vkBotProperties.groupApiKey,
                userId = userId,
                randomId = messageId,
                message = modifyMessage(message),
                version = vkBotProperties.apiVersion,
            ).execute()
        }.onSuccess { response ->
            val responseBody = response.body()
            if (responseBody?.get("error") == null) {
                if (response.isSuccessful) {
                    log.info("Message sent successfully: $responseBody")
                }
            } else {
                log.error("Failed to send message: ${responseBody.toPrettyString()}")
            }
        }.onFailure { e ->
            log.warn("Failed to send message: $e")
        }
    }

    private fun modifyMessage(message: String): String = "Вы сказали: $message"
}
