package biz.lci.springaidemo;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class ChatController {
    private final ChatClient chatClient;

    @Value("${spring.ai.openai.api-key}")
    private String apiKey;

    @Autowired
    public ChatController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/generate") // http://localhost:8080/ai/generate?message=Tell%20me%20a%20joke
    public Map generate(@RequestParam(value = "message", defaultValue = "Tell me a joke") String message) {
        log.info("generate - key={}, message={}", apiKey, message);
        return Map.of("generation", chatClient.generate(message));
    }
}
