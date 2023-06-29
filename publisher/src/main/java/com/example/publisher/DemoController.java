package com.example.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController

public class DemoController {
    private final StreamBridge streamBridge;

    private final Logger log = LoggerFactory.getLogger(DemoController.class);

    public DemoController(StreamBridge streamBridge) {
        this.streamBridge = streamBridge;
    }

    // curl -v localhost:8080 -d '{"text":"Hello"}' -H 'Content-Type: application/json'
    @PostMapping
    public void tweet(@RequestBody Tweet tweet) {
        log.info("publish {}", tweet.text);
        streamBridge.send("output", tweet);

    }

    public static class Tweet {
        public String text;
    }
}
