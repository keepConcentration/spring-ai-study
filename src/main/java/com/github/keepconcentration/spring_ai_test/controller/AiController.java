package com.github.keepconcentration.spring_ai_test.controller;

import java.net.URI;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.content.Media;
import org.springframework.ai.openai.api.OpenAiApi;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

  private final ChatModel chatModel;

  private final ChatClient chatClient;

  public AiController(ChatModel chatModel) {
    this.chatModel = chatModel;
    this.chatClient = ChatClient.create(chatModel);
  }

  @GetMapping("/chat")
  public String question(@RequestParam String question) {
    String call = chatModel.call(question);
    return call;
  }

  @GetMapping("/analyze-image-url")
  public String analyzeImageUrl(
      @RequestParam String imageUrl,
      @RequestParam String question) {
    URI imageUri = URI.create(imageUrl);
    MimeType mimeType = getMimeTypeByUri(imageUri);

    UserMessage userMessage = UserMessage.builder()
        .text(question)
        .media(Media.builder().data(imageUri).mimeType(mimeType).build())
        .build();

    ChatOptions options = ChatOptions.builder()
        .model(OpenAiApi.ChatModel.GPT_4_O.getValue())
        .build();

    // TODO 오류 발견
    // URI with undefined scheme
    ChatResponse response = chatClient.prompt(Prompt.builder()
        .messages(userMessage)
        .chatOptions(options)
        .build())
        .call().chatResponse();

    return response.toString();
  }

  private MimeType getMimeTypeByUri(URI uri) {
    String ext = uri.getPath().substring(uri.getPath().lastIndexOf(".") + 1);
    if ("png".equalsIgnoreCase(ext)) {
      return MimeTypeUtils.IMAGE_PNG;
    } else if ("jpg".equalsIgnoreCase(ext) || "jpeg".equalsIgnoreCase(ext)) {
      return MimeTypeUtils.IMAGE_JPEG;
    } else if ("gif".equalsIgnoreCase(ext)) {
      return MimeTypeUtils.IMAGE_GIF;
    }
    return null;
  }
}
