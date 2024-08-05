package controller

import AbstractComponentTest
import io.restassured.http.ContentType
import io.restassured.module.kotlin.extensions.Given
import io.restassured.module.kotlin.extensions.Then
import io.restassured.module.kotlin.extensions.When
import org.amshove.kluent.`should be equal to`
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
    fun `should return status code 400 for unsupported request type`() {
        Given {
            port(port).contentType(ContentType.JSON).body(unsupportedJson)
        } When {
            post("/")
        } Then {
            statusCode(HttpStatus.BAD_REQUEST.value())
        }
    }

    @Test
    fun `should successfully retry new_message request`() {
        `mock vk api response with error code`(mockServerClient, message, newMessageJson)

        Given {
            port(port).contentType(ContentType.JSON)
                .body(newMessageJson)
        } When {
            post("/")
        } Then {
            statusCode(HttpStatus.INTERNAL_SERVER_ERROR.value())
        }

        val countOfRecordedRequests =
            mockServerClient.retrieveRecordedRequests(HttpRequest.request().withPath("/messages.send")).size
        countOfRecordedRequests.`should be equal to`(retryAttempts.toInt() + 1) // оригинальная попытка + кол-во ретраев
    }
}
