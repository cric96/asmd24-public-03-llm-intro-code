package it.unibo.basics;

import it.unibo.utils.LlmConstants;
import dev.langchain4j.data.message.UserMessage;
import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.model.ollama.OllamaChatModel;

public class TextGenerationExample {
    public static void main(String[] args) {
        final ChatModel model = OllamaChatModel.builder()
            .baseUrl(LlmConstants.OLLAMA_BASE_URL)
            .logRequests(true)
            .logResponses(true)
            .modelName(LlmConstants.CHAT_MODEL_SMOLLM)
            .numPredict(LlmConstants.MAX_PREDICT_TOKENS)
            //.temperature(0.0)
            //.topK(1)
            .build();
        final UserMessage message = UserMessage.userMessage("Say Hello!");
        var response = model.chat(message);
        System.out.println("Token used: " + response.tokenUsage().inputTokenCount());
        System.out.println("Toked in output: " + response.tokenUsage().outputTokenCount());
        System.out.println("Response: " + response.aiMessage().text());
    }
}
