package com.example.subscriber;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

@EnableBinding(Sink.class)
public class MessageListener {

    private final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @StreamListener(Sink.INPUT)
    public void print(Tweet tweet) {
        log.info("Received {}", tweet.text);
    }

    public static class Tweet {
        public String text;
    }
}
