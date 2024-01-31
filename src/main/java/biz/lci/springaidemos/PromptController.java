package biz.lci.springaidemos;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class PromptController {
    @Autowired
    protected ChatClient chatClient;

    @GetMapping("/ai/prompt")
    public Map<String, String> chat(
            @RequestParam(defaultValue = "Tell me an important fact") String userPrompt,
            @RequestParam(defaultValue = "You are a pirate and only like to talk about pirate stuff") String systemPrompt) {
        UserMessage userMessage = new UserMessage(userPrompt);
        SystemMessage systemMessage = new SystemMessage(systemPrompt);
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        ChatResponse chatResponse = chatClient.call(prompt);
        return Map.of(
                "userPrompt", userPrompt,
                "systemPrompt", systemPrompt,
                "response", chatResponse.getResult().getOutput().getContent()
        );
    }
}
