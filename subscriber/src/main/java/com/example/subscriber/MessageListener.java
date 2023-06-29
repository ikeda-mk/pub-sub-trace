package com.example.subscriber;

import java.util.function.Consumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Component("sandboxConsumer")
public class MessageListener implements Consumer<Message<MessageListener.Tweet>>{

    private final Logger log = LoggerFactory.getLogger(MessageListener.class);

    @Override
    public void accept(Message<Tweet> tweet) {
        log.info("Received {}", tweet.getPayload().getText());
    }

    public static class Tweet {
        private String text;

        public String getText() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }
    }
}
