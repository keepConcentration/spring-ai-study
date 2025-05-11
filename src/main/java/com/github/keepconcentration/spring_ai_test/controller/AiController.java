package com.github.keepconcentration.spring_ai_test.controller;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/ai")
public class AiController {

  private final ChatModel chatModel;

  public AiController(ChatModel chatModel) {
    this.chatModel = chatModel;
  }

  @GetMapping("/chat")
  public String question(@RequestParam String question) {
    String call = chatModel.call(question);
    return call;
  }
}
