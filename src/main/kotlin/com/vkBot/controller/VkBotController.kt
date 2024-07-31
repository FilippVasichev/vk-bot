package com.vkBot.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.vkBot.client.config.VkBotProperties
import com.vkBot.service.VkBotService
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class VkBotController(
    private val vkBotService: VkBotService,
    private val vkBotProperties: VkBotProperties,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @PostMapping
    fun handleVkCallback(
        @RequestBody request: VkRequest,
    ): ResponseEntity<String> {
        return when (request.type) {
            "confirmation" -> return ResponseEntity.ok(vkBotProperties.callbackVerificationCode)
            "message_new" ->
                if (request.`object` != null) {
                    vkBotService.reply(
                        request.`object`.message.text,
                        request.`object`.message.userId,
                        request.`object`.message.messageId,
                    )
                    ResponseEntity.ok("")
                } else {
                    log.error("Invalid object: $request")
                    ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid object")
                }

            else -> ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported request type")
        }
    }
}

data class VkRequest(
    val type: String,
    val `object`: MessageField?,
)

data class MessageField(
    @JsonProperty("message")
    val message: MessageDetails,
)

data class MessageDetails(
    @JsonProperty("id")
    val messageId: Int,
    @JsonProperty("from_id")
    val userId: Int,
    val text: String,
)
