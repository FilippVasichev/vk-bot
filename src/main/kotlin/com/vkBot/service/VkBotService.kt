package com.vkBot.service

import com.vkBot.client.VkBotApi
import com.vkBot.client.config.VkBotProperties
import com.vkBot.controller.MessageField
import com.vkBot.controller.VkRequest
import io.github.resilience4j.retry.annotation.Retry
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
@Retry(name = "VkService")
class VkBotService(
    private val vkBotApi: VkBotApi,
    private val vkBotProperties: VkBotProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    fun handle(request: VkRequest): Result<String> =
        when (request.type) {
            "confirmation" -> Result.success(vkBotProperties.callbackVerificationCode)
            "message_new" -> reply(request.`object`!!)
            else -> Result.failure(IllegalArgumentException("Invalid object: $request"))
        }

    private fun reply(messageRequest: MessageField): Result<String> =
        messageRequest.message.let {
            vkBotApi.sendMessage(
                groupApiKey = vkBotProperties.groupApiKey,
                userId = it.userId,
                randomId = it.messageId,
                message = modifyMessage(it.text),
                version = vkBotProperties.apiVersion,
            ).execute().let { response ->
                if (!response.isSuccessful) {
                    log.error("Failed to send a message: ${response.errorBody()}")
                    throw RuntimeException("Connection failed with status code:" + response.code())
                } else {
                    return Result.success("Message sent successfully")
                }
            }
        }

    private fun modifyMessage(message: String): String = "Вы сказали: $message"
}
