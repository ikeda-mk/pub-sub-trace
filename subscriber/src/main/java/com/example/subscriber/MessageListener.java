package com.example.subscriber;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.ExtraFieldPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;

@EnableBinding(Sink.class)
public class MessageListener {

    private final RestTemplateBuilder restTemplateBuilder;

    private final Logger log = LoggerFactory.getLogger(MessageListener.class);

    private final Tracer tracer;
    private final Tracing tracing;
//    private final RestTemplate restTemplate;

    public MessageListener(RestTemplateBuilder restTemplateBuilder, Tracer tracer, Tracing tracing) {
        this.restTemplateBuilder = restTemplateBuilder;
        this.tracer = tracer;
        this.tracing = tracing;
//        this.restTemplate = restTemplate;
    }

    @StreamListener(Sink.INPUT)
    public void print(Tweet tweet) throws URISyntaxException {

        Span c = tracer.currentSpan();
        Span s = tracer.nextSpan();
//        tracing.tracer().nextSpan().context().spanId();

        log.info("Received {} {}", tweet.text, ExtraFieldPropagation.get("x-country-code"));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON_UTF8);

//        RequestEntity<?> req = new RequestEntity<>(tweet, headers, HttpMethod.POST.POST, );

//        restTemplate.postForObject(new URI("http://localhost:8082/demo3"), tweet, String.class);


        String response = restTemplateBuilder.build().postForObject(new URI("http://localhost:8082/demo3"), tweet, String.class);
    }

    public static class Tweet {
        public String text;
    }
}
