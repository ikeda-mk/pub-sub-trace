package com.example.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;


@RestController
@EnableBinding(Source.class)
public class DemoController {
    private final Source source;

    private final Logger log = LoggerFactory.getLogger(DemoController.class);

    public DemoController(Source source) {
        this.source = source;
    }

    // curl -v localhost:8080 -d '{"text":"Hello"}' -H 'Content-Type: application/json'
    @PostMapping
    public void tweet(@RequestBody Tweet tweet) {
        log.info("publish {}", tweet.text);
        source.output().send(MessageBuilder.withPayload(tweet).build());
    }

    public static class Tweet {
        public String text;
    }
}
