package biz.lci.springaidemo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ChatControllerTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void generate() {
        webTestClient.get().uri("/ai/generate?message=Tell%20me%20a%20joke")
                .exchange()
                .expectStatus().isOk()
                .expectBody()
                .jsonPath("$.generation").isNotEmpty();
    }
}