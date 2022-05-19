package io.stock.kr.calculator.config;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.time.Duration;

@Testcontainers
@ActiveProfiles("test-containers")
@SpringBootTest
public class TestContainerConnectionTest {

    static final DockerComposeContainer container;

    static {
        container = new DockerComposeContainer(new File("src/test/resources/docker/docker-compose/docker-compose.yml"))
                .withExposedService(
                        "redis-normal",
                        16379,
                        Wait.forListeningPort().withStartupTimeout(Duration.ofSeconds(15)))
                .withLocalCompose(true);
        container.start();
    }

    @Test
    public void TEST_lOADING(){

    }
}
