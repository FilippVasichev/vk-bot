package com.vkBot.controller

import com.fasterxml.jackson.annotation.JsonProperty
import com.vkBot.service.VkBotService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/")
class VkBotController(
    private val vkBotService: VkBotService,
) {
    @PostMapping
    fun handleVkCallback(
        @RequestBody request: VkRequest,
    ): ResponseEntity<String> =
        vkBotService.handle(request).let { result ->
            if (result.isSuccess) {
                ResponseEntity.ok(result.getOrNull())
            } else {
                ResponseEntity.badRequest().build()
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
