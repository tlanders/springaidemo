package biz.lci.springaidemos;

import org.springframework.ai.chat.ChatClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class ChatController {
    @Autowired
    protected ChatClient chatClient;

    @GetMapping("/chat")
    public Map<String,String> chat(@RequestParam(defaultValue = "Tell me a joke") String prompt) {
        return Map.of(
                "response", chatClient.call(prompt),
                "prompt", prompt
        );
    }
}
