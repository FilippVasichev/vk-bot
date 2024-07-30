package com.vkBot.client

import com.fasterxml.jackson.databind.JsonNode
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
Retrofit2 HTTP client configuration for making GET call to vk API.
 */

interface VkBotApi {
    @GET("messages.send")
    fun sendMessage(
        @Query("access_token") groupApiKey: String,
        @Query("user_id") userId: Int,
        @Query("random_id") randomId: Int,
        @Query("message") message: String,
        @Query("v") version: Double,
    ): Call<JsonNode>
}
