package controller

import AbstractComponentTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest.request
import org.mockserver.model.HttpResponse.response
import org.mockserver.model.Parameter


class TestVkBotController : AbstractComponentTest() {

    @Test
    fun `should successfully call vk for app authentication`() {
        MockServerClient("localhost", port)
            .`when`(
                request()
                    .withMethod("POST")
                    .withPath("/")
                    .withBody(confirmationJson)
            ).respond(
                response()
                    .withStatusCode(200)
                    .withBody(secretCode)
            )

        Given {
            port(port)
            contentType(ContentType.JSON)
            body(confirmationJson)
        } When {
            post("/")
        } Then {
            statusCode(200)
            body(equalTo(secretCode))
        }
    }

    @Test
    fun `should successfully handle message_new request and call sendMessage`() {
        MockServerClient("localhost", port).`when`(
            request()
                .withMethod("GET")
                .withPath("https://api.vk.com/method/messages.send")
                .withQueryStringParameters(
                    Parameter.param("access_token", vkBotProperties.groupApiKey),
                    Parameter.param("user_id", "12345"),
                    Parameter.param("random_id", "12345"),
                    Parameter.param("message", "Вы сказали: $message"),
                    Parameter.param("v", vkBotProperties.apiVersion.toString())
                )
        )

        Given {
            port(port)
                .contentType(ContentType.JSON)
                .body(newMessageJson)
        } When {
            post("/")
        } Then {
            statusCode(200)
        }
    }
}

