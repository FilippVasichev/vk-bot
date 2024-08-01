package com.vkBot.config

import com.vkBot.client.VkBotApi
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
Configuration class responsible for creating HTTP clients for vk API.
 */

@Configuration
class HttpClientConfig {
    @Value("\${vk.baseUrl}")
    lateinit var baseUrl: String

    @Bean
    fun vkApiHttpClient(): VkBotApi {
        val httpClient: OkHttpClient = OkHttpClient.Builder().build()
        val retrofit =
            Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
        return retrofit.create(VkBotApi::class.java)
    }
}
