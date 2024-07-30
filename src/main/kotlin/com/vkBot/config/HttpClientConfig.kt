package com.vkBot.config

import com.vkBot.client.VkBotApi
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

/**
Configuration class responsible for creating HTTP clients for vk API.
 */

@Configuration
class HttpClientConfig {
    @Bean
    fun vkApiHttpClient(): VkBotApi {
        val httpClient: OkHttpClient = OkHttpClient.Builder().build()
        val retrofit =
            Retrofit.Builder()
                .baseUrl("https://api.vk.com/method/")
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
        return retrofit.create(VkBotApi::class.java)
    }
}
