package it.unibo.chat;

import dev.langchain4j.data.message.ChatMessage;
import dev.langchain4j.model.chat.ChatModel;

import java.util.List;

/**
 * An interface for a chat agent that can interact with other agents or users.
 * It can record messages and interact with the user or other agents.
 * Therefore, it is based on a chat session.
 */
public interface ChatAgent {
    /**
     * Interact with the user or other agents.
     * @param userMessage The message from the user or other agents.
     * @return The response from the agent.
     */
    String interact(String userMessage);

    /**
     * @return reply based on the current message flow
     */
    String interact();

    /**
     * Record a message in the message flow.
     * @param message The message to be recorded.
     */
    void recordMessage(ChatMessage message);

    /**
     * Create a chat agent with the given model and initial message.
     * @param model The model to be used for chatting.
     * @param initialMessage The initial message to be used for the chat.
     * @return The chat agent created.
     */
    static ChatAgent createChatAgent(ChatModel model, List<ChatMessage> initialMessage) {
        return new ChatAgentBase(model, initialMessage);
    }
}
