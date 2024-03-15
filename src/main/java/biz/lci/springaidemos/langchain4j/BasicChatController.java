package biz.lci.springaidemos.langchain4j;

import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai/langchain")
public class BasicChatController {
    @Value("${spring.ai.openai.api-key}")
    private String OPENAI_API_KEY;

    @GetMapping("/chat")
    public String basicChat(@RequestParam(defaultValue = "Hello AI!") String prompt) {
        ChatLanguageModel model = OpenAiChatModel.withApiKey(OPENAI_API_KEY);

        return model.generate(prompt);
    }
}
