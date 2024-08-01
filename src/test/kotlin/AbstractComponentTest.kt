import com.vkBot.VkBotApplication
import com.vkBot.client.config.VkBotProperties
import org.mockserver.client.MockServerClient
import org.mockserver.model.HttpRequest
import org.mockserver.model.HttpResponse
import org.mockserver.model.Parameter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.context.annotation.Import
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(
    classes = [VkBotApplication::class],
    initializers = [MockServerContainerInitializer::class],
)
@Import(MockServerConfig::class)
abstract class AbstractComponentTest {
    @Autowired
    lateinit var vkBotProperties: VkBotProperties

    @Autowired
    lateinit var mockServerClient: MockServerClient

    @LocalServerPort
    var port: Int = 0

    fun mockVkApiResponse(
        mockServerClient: MockServerClient,
        message: String,
        body: String,
    ) {
        mockServerClient.`when`(
            HttpRequest.request().withMethod(HttpMethod.GET.name())
                .withPath("/messages.send")
                .withQueryStringParameters(
                    Parameter.param("access_token", vkBotProperties.groupApiKey),
                    Parameter.param("user_id", "12345"),
                    Parameter.param("random_id", "12345"),
                    Parameter.param("message", "Вы сказали: $message"),
                    Parameter.param("v", vkBotProperties.apiVersion.toString()),
                ),
        ).respond(
            HttpResponse.response()
                .withBody(body)
                .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .withStatusCode(HttpStatus.OK.value()),
        )
    }
}
