package biz.lci.springaidemos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.Generation;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.transformer.splitter.TextSplitter;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
public class RecommendationService {
    // 170:00 into Ken Kousen Spring AI O'Reilly video

    @Value("classpath:/prompts/products-system-message.st")
    protected Resource systemMessagePrompt;

//    @Value("classpath:/data/au2.json")
    @Value("classpath:/data/skus-all-en-us.json")
    protected Resource productDataJson;

    @Autowired
    protected ChatClient chatClient;

    @Autowired
    protected EmbeddingClient embeddingClient;

    public Generation recommend(String message) {
        log.info("recommend - message={}", message);

        SimpleVectorStore vectorStore = new SimpleVectorStore(embeddingClient);
        File productVectorStoreFile = new File("src/main/resources/data/productVectorStore.json");
        if(productVectorStoreFile.exists()) {
            vectorStore.load(productVectorStoreFile);
        } else {
            log.debug("loading product json into vector store");

            JsonReader jsonReader = new JsonReader(productDataJson,
                    "brand",
                    "sku",
                    "productname",
                    "form",
                    "species",
                    "breedsize",
                    "productdesc",
                    "searchdescription",
                    "feedingguide",
                    "nutrition",
                    "howitworks",
                    "howithelps",
                    "additionalinformation",
                    "ingredients",
                    "productimageurl",
                    "productpageurl",
                    "packagedesc",
                    "packagesize",
                    "packageunits",
                    "superfamily");
            List<Document> documents = jsonReader.get();

            TextSplitter textSplitter = new TokenTextSplitter();
            List<Document> splitDocuments = textSplitter.apply(documents);

            log.debug("creating embeddings");
            vectorStore.add(splitDocuments);

            log.debug("saving embeddings");
            vectorStore.save(productVectorStoreFile);
        }

        log.debug("Finding similar documents");
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.query(message).withTopK(11));

        log.debug("Found {} similar documents", similarDocuments.size());

        similarDocuments.forEach(doc -> log.debug("doc: {}", doc));

        Message systemMessage = getSystemMessage(similarDocuments);
        UserMessage userMessage = new UserMessage(message);

        log.debug("Asking AI model to reply to question.");
        Prompt prompt = new Prompt(List.of(userMessage, systemMessage));
        log.debug("prompt: {}", prompt);

        ChatResponse chatResponse = chatClient.call(prompt);
        log.debug("chatResponse: {}", chatResponse.getResult().toString());

        return chatResponse.getResult();
    }

    protected Message getSystemMessage(List<Document> similarDocuments) {
        String documents = similarDocuments.stream()
                .map(Document::getContent)
                .collect(Collectors.joining("\n"));

        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemMessagePrompt);
        return systemPromptTemplate.createMessage(Map.of("petFood", documents));
    }
}
