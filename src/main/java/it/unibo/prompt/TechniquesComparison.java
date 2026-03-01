package it.unibo.prompt;

import it.unibo.utils.LlmConstants;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.embedding.DimensionAwareEmbeddingModel;
import dev.langchain4j.model.ollama.OllamaChatModel;
import dev.langchain4j.model.ollama.OllamaEmbeddingModel;
import it.unibo.prompt.few.FewShotAgent;
import it.unibo.prompt.zero.ZeroShotAgent;
import it.unibo.utils.Pair;

import java.util.List;
import java.util.stream.IntStream;

public class TechniquesComparison {
    public static void main(String[] args) {
        final ChatModel model = OllamaChatModel.builder()
            .baseUrl(LlmConstants.OLLAMA_BASE_URL)
            .logRequests(true)
            .logResponses(true)
            .modelName(LlmConstants.CHAT_MODEL_QWEN)
            .numPredict(LlmConstants.MAX_PREDICT_TOKENS)
            .build();
        final DimensionAwareEmbeddingModel embeddingModel = OllamaEmbeddingModel.builder()
            .baseUrl(LlmConstants.OLLAMA_BASE_URL)
            .logRequests(true)
            .logResponses(true)
            .modelName(LlmConstants.EMBEDDING_MODEL)
            .build();
        var zeroShot = new ZeroShotAgent(model, "Just reply with the RIGHT number.");
        var fewShot = new FewShotAgent(model, List.of(
            FewShotAgent.QuestionAnswer.from("Yesterday I ate 6 apple, today 3. How many apple I have today?", "3"),
            FewShotAgent.QuestionAnswer.from("Today I have 6 apple, I eat 3, How many apple I have today?", "3"),
            FewShotAgent.QuestionAnswer.from("Yesterday I have 6 apple, today i eat 1, how many apple i have today?", "5")
        ));
        List<PromptBasedAgent> agents = List.of(
            zeroShot, fewShot
        );
        var evaluator = new CosineEmbeddingEvaluator(embeddingModel);
        var question = """
            Q: Today I have 6 apples. Tomorrow I buy 3 more. Yesterday I ate 6
            apples, How many apples do I have TODAY?
        """;
        var groundTruth = "today I have 6 apple";
        var responses = agents.stream()
            .map(agent -> agent.ask(question))
            .toList();
        var scores = agents.stream()
            .map(agent -> evaluator.compare(groundTruth, agent.ask(question)))
            .toList();
        var scoreWithIndex = IntStream.range(0, agents.size())
            .mapToObj(i -> Pair.of(i, scores.get(i)))
            .toList();
        var sortedScores = scoreWithIndex.stream()
            .sorted((a, b) -> Double.compare(b.y(), a.y()))
            .toList();
        System.out.println("Question: " + question);
        // best
        var best = sortedScores.getFirst().x();
        var worst = sortedScores.getLast().x();
        System.out.println("Best: " + agents.get(best) + " with response: " + responses.get(best));
        System.out.println("Worst: " + agents.get(worst) + " with response: " + responses.get(worst));
    }
}
