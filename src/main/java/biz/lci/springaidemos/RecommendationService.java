package biz.lci.springaidemos;

import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.ChatClient;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingClient;
import org.springframework.ai.reader.JsonReader;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Slf4j
@Service
public class RecommendationService {
    // 170:00 into Ken Kousen Spring AI O'Reilly video

    @Value("classpath:/prompts/products-system-message.st")
    protected Resource systemMessagePrompt;

//    @Value("classpath:/data/au2.json")
    @Value("classpath:/data/skus-20240220-190811-en-us.json")
    protected Resource productDataJson;

    @Autowired
    protected ChatClient chatClient;

    @Autowired
    protected EmbeddingClient embeddingClient;

    public List<Document> recommend(String message) {
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

            log.debug("creating embeddings");
            vectorStore.add(documents);

            log.debug("saving embeddings");
            vectorStore.save(productVectorStoreFile);
        }

        log.debug("Finding similar documents");
        List<Document> similarDocuments = vectorStore.similaritySearch(
                SearchRequest.query(message).withTopK(5));

        log.debug("Found {} similar documents", similarDocuments.size());
        similarDocuments.forEach(doc -> log.debug("doc: {}", doc));

        return similarDocuments;
    }
}
