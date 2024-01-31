package biz.lci.springaidemos;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class RoleController {
    private final ChatClient chatClient;

    @Value("classpath:/prompts/system-message.st")
    private Resource systemMessage;

    @Autowired
    public RoleController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/roles")
    public List<Generation> generate(
            @RequestParam(value="message", defaultValue="Tell me about three famous pirates from the Golden Age of Piracy") String message,
            @RequestParam(value="name", defaultValue="bob") String name,
            @RequestParam(value="voice", defaultValue="pirate") String voice
    ) {
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessage);
        Message systemMessage = systemPromptTemplate.createMessage(Map.of("name", name, "voice", voice));

        Prompt prompt = new Prompt(List.of(new UserMessage(message), systemMessage));

        return chatClient.call(prompt).getResults();
    }
}
