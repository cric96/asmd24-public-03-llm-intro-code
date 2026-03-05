# Exercise: AI-Powered Interactive Story (MVC)

Build an interactive text-adventure game where an LLM generates the narrative, questions, and
choices dynamically.
The player makes decisions that affect their stats until the story ends.

## Exercise 0 — Study and Understand

The provided codebase contains the main abstractions and implementations for a simple interactive
story engine. It is organised into the following packages:

| Package | Contents |
|---|---|
| `e1.model` | `Story` , `StoryImpl`, `Player`, `StoryResponse` — the domain model. |
| `e1.engine` | `StoryEngine`, `LLMStoryEngine`, `JsonCodec` — LLM communication layer. |
| `e1.prompt` | `StoryPrompt`, `BeginPrompt`, `AdvancePrompt` — prompt construction. |
| `e1.view` | `StoryView`, `ConsoleStoryView` — user interaction (console-based). |
| `e1` | `StoryApp` — the application entry point and game loop. |

Read through the code, understand how the pieces fit together, and identify where you will
work in the exercises below.

---

## Exercise 1 — StoryPrompt (`e1.prompt`)

Look at the `StoryPrompt` interface and its implementations.
Your task is to create two concrete prompt classes:

1. **`BeginPrompt`** — for starting a new story.
   It receives the player's name, initial stats, and a setting description.
2. **`AdvancePrompt`** — for advancing the story.
   It receives the current player state, the previous question, and the player's chosen action.

When implementing `toPromptString()`, ensure the generated prompt is well-structured so the LLM
can understand it. In particular:

- Include clear instructions for the LLM to produce a narrative, a question, and a list of choices.
- Follow prompt-engineering best practices: be explicit about the response format you expect
  (e.g., a JSON object with specific fields) and provide an example if necessary.
- Look at `StoryResponse` — the LLM's reply must match its fields (`narrative`, `question`,
  `choices`, `updatedPlayer`, `gameOver`), because `JsonCodec` will deserialise the raw text
  directly into that record.

---

## Exercise 2 — Test LLMStoryEngine (`e1.engine`)

`LLMStoryEngine` is the implementation of `StoryEngine` that delegates to an LLM via `ChatModel`.
It already handles retries and JSON decoding; your task is to **test** that the surrounding logic
is consistent with the expected flow.

Using a **mocked** `ChatModel` (e.g., with Mockito), verify the following scenarios:

1. **Happy path** — the model returns valid JSON on the first attempt; the engine parses it
   correctly and returns a `StoryResponse`.
2. **Retry and recover** — the model returns garbage on the first attempt(s) but valid JSON on a
   later attempt; the engine retries and eventually succeeds.
3. **All retries exhausted** — the model never returns valid JSON; the engine throws after
   the configured number of retries.
4. **Malformed JSON** — the model returns syntactically invalid JSON; the engine treats it as a
   failed attempt and retries.

---

## Bonus — R&D and Extensions

These are open-ended tasks for further exploration and experimentation.

- **Inventory system:**
  Add a `List<String> inventory` field to `Player` and update the prompt templates so the LLM is
  aware of the player's items. The LLM can then offer choices that depend on held items
  (e.g., "Use the healing potion" only appears if the player has one). Think about how the
  `StoryResponse` should communicate inventory changes back to the caller.

- **Story memory / context window:**
  LLM calls are stateless — each request is independent. Create a `ContextPrompt` (or a
  prompt decorator) that appends a summary of previous beats to the current prompt, giving the
  LLM long-term coherence. Consider the trade-off between context length and token cost, and
  experiment with summarisation strategies (e.g., keep only the last *N* beats, or ask the LLM
  to condense the history into a single paragraph).

