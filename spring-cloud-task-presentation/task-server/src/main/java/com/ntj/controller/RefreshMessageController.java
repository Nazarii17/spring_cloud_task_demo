package com.ntj.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
@RequestMapping("/refresh/message")
public class RefreshMessageController {

    @Value("${application.refresh-message}")
    private String message;

    @GetMapping()
    public String refreshMessage() {
        return message;
    }
}
