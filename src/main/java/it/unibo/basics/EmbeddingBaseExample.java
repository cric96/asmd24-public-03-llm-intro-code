package it.unibo.basics;

import it.unibo.utils.LlmConstants;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import it.unibo.utils.Vector;

import java.util.List;
import java.util.stream.Stream;

public class EmbeddingBaseExample {
    public static void main(String[] args) {
        DimensionAwareEmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
            .baseUrl(LlmConstants.OLLAMA_BASE_URL)
            .modelName(LlmConstants.EMBEDDING_MODEL)
            .logRequests(true)
            .logResponses(true)
            .build();
        // Dataset
        final var data = List.of("Hello", "how", "are", "you");
        List<Vector> result = data.stream()
            .map(embeddingModel::embed)
            .map(response -> response.content().vector())
            .map(Vector::fromFloatArray)
            .toList();
        System.out.println("Embedding size: " + result.getFirst().getData().length);
        // Example: contextual distance
        final Vector anotherSentence = Vector.fromFloatArray(
            embeddingModel.embed("Hi").content().vector()
        );
        final List<Double> distances = result.stream()
            .map(vector -> vector.distance(anotherSentence))
            .toList();
        final List<Double> similarity = result.stream()
            .map(vector -> vector.cosineSimilarity(anotherSentence))
            .toList();
        System.out.println("Dataset: " + data);
        System.out.println("Distances: " + distances);
        System.out.println("Similarity: " + similarity);
    }
}
