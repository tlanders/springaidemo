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
public class RecommendationController {
    @Autowired
    protected RecommendationService recommendationService;

    @GetMapping("/ai/recommend")
    public Map<String,Object> recommend(@RequestParam(defaultValue = "I have a dog") String message) {
        log.info("recommend - message={}", message);
        return Map.of(
                "recommendation", recommendationService.recommend(message).getOutput().getContent(),
                "message", message
        );
    }
}
