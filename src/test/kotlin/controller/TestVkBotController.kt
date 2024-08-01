package controller

import AbstractComponentTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.hamcrest.CoreMatchers.equalTo
import org.junit.jupiter.api.Test
import org.mockserver.model.HttpRequest
import org.mockserver.model.Parameter
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus

class TestVkBotController : AbstractComponentTest() {
    @Test
    fun `should successfully call vk for app authentication`() {
        val confirmationJson = "{ \"type\": \"confirmation\", \"group_id\": 11111111 }"
        Given {
            port(port)
            contentType(ContentType.JSON)
            body(confirmationJson)
        } When {
            post("/")
        } Then {
            statusCode(HttpStatus.OK.value())
            body(equalTo(vkBotProperties.callbackVerificationCode))
        }
    }

    @Test
    fun `should successfully handle message_new request and call sendMessage`() {
        val responseBody = "{\"response\":1}"
        val message = "test message"
        val newMessageJson =
            """
            {
              "type": "message_new",
              "object": {
                "message": {
                  "id": 12345,
                  "from_id": 12345,
                  "text": "$message"
                }
              },
              "group_id": 11111111
            }
            """.trimIndent()

        mockVkApiResponse(mockServerClient, message, responseBody)

        Given {
            port(port).contentType(ContentType.JSON).body(newMessageJson)
        } When {
            post("/")
        } Then {
            statusCode(HttpStatus.OK.value())
        }

        mockServerClient.verify(
            HttpRequest.request().withMethod(HttpMethod.GET.name())
                .withPath("/messages.send")
                .withQueryStringParameters(
                    Parameter.param("access_token", vkBotProperties.groupApiKey),
                    Parameter.param("user_id", "12345"),
                    Parameter.param("random_id", "12345"),
                    Parameter.param("message", "Вы сказали: $message"),
                    Parameter.param("v", vkBotProperties.apiVersion.toString()),
                ),
        )
    }

    @Test
    fun `should return status code 400 for unsupported request type`()  {
        val unsupportedJson = "{ \"type\": \"unsupported\", \"group_id\": 11111111 }"
        Given {
            port(port).contentType(ContentType.JSON).body(unsupportedJson)
        } When {
            post("/")
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
        }
    }
}
