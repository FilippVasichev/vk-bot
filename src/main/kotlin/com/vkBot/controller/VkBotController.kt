package com.vkBot.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.vkBot.service.VkBotService
import com.vkBot.service.config.VkBotProperties
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
    @PostMapping
    fun handleVkCallback(
        @RequestBody request: VkRequest,
    ): ResponseEntity<String> {
        println(request.`object`.toString())
        if (request.type == "confirmation") return ResponseEntity.ok(vkBotProperties.callbackVerificationCode)
        if (request.type == "message_new") {
            vkBotService.reply(request.`object`.message.text, request.`object`.message.userId)
            return ResponseEntity.ok("")
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Unsupported request type")
    }
}

data class VkRequest(
    val type: String,
    val `object`: MessageField,
)

data class RequestError(
    val errorCode: Int,
    val errorMessage: String,
)

data class MessageField(
    @JsonProperty("message")
    val message: MessageDetails,
)

data class MessageDetails(
    val userId: Int,
    val text: String,
)
