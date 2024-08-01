import org.mockserver.client.MockServerClient
import org.springframework.boot.test.util.TestPropertyValues
import org.springframework.context.ApplicationContextInitializer
import org.springframework.context.ConfigurableApplicationContext
import org.testcontainers.containers.MockServerContainer
import org.testcontainers.utility.DockerImageName

class MockServerContainerInitializer private constructor() :
    ApplicationContextInitializer<ConfigurableApplicationContext>,
    MockServerContainer(
        DockerImageName
            .parse("mockserver/mockserver")
            .withTag("mockserver-" + MockServerClient::class.java.getPackage().implementationVersion),
    ) {
        companion object {
            private val instance: MockServerContainerInitializer =
                MockServerContainerInitializer()
        }

        override fun initialize(applicationContext: ConfigurableApplicationContext) {
            instance.start()
            TestPropertyValues.of(
                "vk.baseUrl = ${instance.endpoint}",
                "mockserver.host = ${instance.host}",
                "mockserver.serverPort = ${instance.serverPort}",
            ).applyTo(applicationContext)
        }
    }
