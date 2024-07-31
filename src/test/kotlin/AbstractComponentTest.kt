import com.vkBot.VkBotApplication
import com.vkBot.client.VkBotApi
import com.vkBot.client.config.VkBotProperties
import okhttp3.OkHttpClient
import org.junit.jupiter.api.BeforeEach
import org.mockserver.integration.ClientAndServer
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Profile
import org.springframework.test.context.ContextConfiguration
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory

@Profile("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = [VkBotApplication::class])
abstract class AbstractComponentTest {

    @Autowired
    lateinit var vkBotProperties: VkBotProperties

    var port: Int = 1080
    var secretCode = "secret code"
    var message = "Hello привет"
    var confirmationJson: String = "{ \"type\": \"confirmation\", \"group_id\": 11111111 }"
    var newMessageJson: String = """
    {
      "type": "message_new",
      "object": {
        "message": {
          "id": 12345,
          "from_id": 12345,
          "text": "Hello привет"
        }
      },
      "group_id": 11111111
    }
""".trimIndent()

    fun vkApiHttpClient(): VkBotApi {
        val httpClient: OkHttpClient = OkHttpClient.Builder().build()
        val retrofit =
            Retrofit.Builder()
                .baseUrl("http://localhost:1080/") // ??
                .client(httpClient)
                .addConverterFactory(JacksonConverterFactory.create())
                .build()
        return retrofit.create(VkBotApi::class.java)
    }


    @BeforeEach
    fun startMockServer() {
        ClientAndServer.startClientAndServer(1080).reset()
    }

}