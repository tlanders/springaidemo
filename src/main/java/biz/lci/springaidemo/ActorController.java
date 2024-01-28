package biz.lci.springaidemo;

import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.parser.BeanOutputParser;
import org.springframework.ai.parser.OutputParser;
import org.springframework.ai.prompt.Prompt;
import org.springframework.ai.prompt.PromptTemplate;
import org.springframework.ai.prompt.messages.UserMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ActorController {
    public record ActorsFilms(String actor, List<String> movies) {}

    private final ChatClient chatClient;

    @Autowired
    public ActorController(ChatClient chatClient) {
        this.chatClient = chatClient;
    }

    @GetMapping("/ai/output")
    public ActorsFilms generate(@RequestParam(value="actor", defaultValue="Tom Hanks") String actor) {

        var outputParser = new BeanOutputParser<>(ActorsFilms.class);

        String userMessage = """
            Generate the filmography of the actor {actor}.
            In your output,
                    please do NOT include the backticks and json expression, as in
                    ```json (and the corresponding closing backticks). Just include
                    {format}.
                    """;

        PromptTemplate promptTemplate = new PromptTemplate(userMessage,
                Map.of("actor", actor, "format", outputParser.getFormat()));
        Prompt prompt = promptTemplate.create();

        Generation generation = chatClient.generate(prompt).getGeneration();
        return outputParser.parse(generation.getContent());
    }
}
