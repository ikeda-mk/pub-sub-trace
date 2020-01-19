package com.example.service3;

import brave.Tracer;
import brave.Tracing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController("/demo3")
public class DemoController {

    private final Logger log = LoggerFactory.getLogger(DemoController.class);

    private final Tracer tracer;
    private final Tracing tracing;

    public DemoController(Tracer tracer, Tracing tracing) {
        this.tracer = tracer;
        this.tracing = tracing;
    }

    // curl -v localhost:8080/demo3 -d '{"text":"Hello"}' -H 'Content-Type: application/json'
    @PostMapping
    public void tweet(@RequestBody Tweet tweet, @RequestHeader HttpHeaders headers) {
        tracing.propagation();

        log.info("publish {}", tweet.text);
    }

    public static class Tweet {
        public String text;
    }
}
