import org.mockserver.client.MockServerClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile

@Profile("test")
@Configuration
class MockServerConfig {
    @Bean
    fun mockServerClient(
        @Value("\${mockserver.host}") host: String,
        @Value("\${mockserver.serverPort}") serverPort: Int,
    ): MockServerClient = MockServerClient(host, serverPort)
}
