package biz.lci.springaidemos.langchain4j;

import biz.lci.springaidemos.domain.Product;
import com.fasterxml.jackson.databind.ObjectMapper;
import dev.langchain4j.data.message.AiMessage;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.memory.ChatMemory;
import dev.langchain4j.memory.chat.MessageWindowChatMemory;
import dev.langchain4j.model.chat.ChatLanguageModel;
import dev.langchain4j.model.openai.OpenAiChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/ai/langchain")
public class BasicChatController {
    @Value("${spring.ai.openai.api-key}")
    private String OPENAI_API_KEY;

    @Value("classpath:/data/skus-all-en-us.json")
    protected Resource skuDataJson;

    @Value("classpath:/data/products-partial-20240407-172836-en-us.json")
    protected Resource productDataJson;

    interface Assistant {
        String chat(String prompt);
    }

    @GetMapping(value = "/chat")
    public ResponseEntity<String> basicChat(@RequestParam(defaultValue = "Hello AI!") String prompt) {
        ChatLanguageModel model = OpenAiChatModel.withApiKey(OPENAI_API_KEY);

        AiMessage responseMessage = model.generate(UserMessage.from(prompt)).content();
        log.debug("basicChat - response={}", responseMessage);
        return ResponseEntity.ok(responseMessage.text());
    }

    @GetMapping(value = "/chatWithMemory")
    public ResponseEntity<String> basicChatWithMemory(
            @RequestParam(defaultValue = "Hello AI!") String prompt,
            HttpSession session) {
        ChatMemory memory = (ChatMemory) session.getAttribute("memory");
        if(memory == null) {
            memory = MessageWindowChatMemory.withMaxMessages(10);
            session.setAttribute("memory", memory);
        }

        Assistant assistant = AiServices.builder(Assistant.class)
                .chatLanguageModel(OpenAiChatModel.withApiKey(OPENAI_API_KEY))
                .chatMemory(memory)
                .build();

        String response = assistant.chat(prompt);

        log.debug("basicChat - response={}", response);
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = "/recommendFood")
    public ResponseEntity<String> recommendFood(@RequestParam(defaultValue = "I have a dog") String message) throws Exception {
        // read json docs
        final ObjectMapper objectMapper = new ObjectMapper();

        List<Product> products = objectMapper.readValue(skuDataJson.getFile(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));


        return ResponseEntity.ok("I recommend you try the chicken.");
    }

    @GetMapping(value = "/recommendProduct")
    public ResponseEntity<String> recommendProduct(@RequestParam(defaultValue = "I have a dog") String message) throws Exception {
        // read json docs
        final ObjectMapper objectMapper = new ObjectMapper();

        List<Product> products = objectMapper.readValue(productDataJson.getFile(),
                objectMapper.getTypeFactory().constructCollectionType(List.class, Product.class));


        return ResponseEntity.ok("I recommend you try the chicken. Product count=" + products.size());
    }
}
