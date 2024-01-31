package biz.lci.springaidemos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class ChatController {
    @Autowired
    protected ChatClient chatClient;

    @GetMapping("/ai/chat")
    public Map<String,String> chat(@RequestParam(defaultValue = "Tell me a joke") String prompt) {
        log.info("generate - prompt={}", prompt);
        return Map.of(
                "generation", chatClient.call(prompt),
                "prompt", prompt
        );
    }
}
