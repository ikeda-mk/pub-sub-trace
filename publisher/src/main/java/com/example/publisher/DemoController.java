package com.example.publisher;

import brave.Tracer;
import brave.Tracing;
import brave.propagation.B3Propagation;
import brave.propagation.ExtraFieldPropagation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;


@RestController
@EnableBinding(Source.class)
public class DemoController {
    private final Source source;

    private final Logger log = LoggerFactory.getLogger(DemoController.class);

    private final Tracer tracer;
    private final Tracing tracing;

    private final RestTemplateBuilder restTemplateBuilder;

    public DemoController(Source source, Tracer tracer, Tracing tracing, RestTemplateBuilder restTemplateBuilder) {
        this.source = source;
        this.tracer = tracer;
        this.tracing = tracing;
        this.restTemplateBuilder = restTemplateBuilder;
    }

    // curl -v localhost:8080 -d '{"text":"Hello"}' -H 'Content-Type: application/json'
    @PostMapping
    public void tweet(@RequestBody Tweet tweet) throws URISyntaxException {

        Tracing.Builder builder = Tracing.newBuilder().propagationFactory(
                ExtraFieldPropagation.newFactoryBuilder(B3Propagation.FACTORY)
                        .addField("x-vcap-request-id")
                        .addPrefixedFields("x-baggage-", Arrays.asList("country-code", "user-id"))
                        .build()
        );
        builder.build();

        ExtraFieldPropagation.set("x-country-code", "JP");
        String countryCode = ExtraFieldPropagation.get("x-country-code");

        tracing.currentTraceContext();

//        for (int i = 0; i < 3; i++) {

        log.info("publish {}", tweet.text);
        source.output().send(MessageBuilder.withPayload(tweet).build());



//        String response = restTemplateBuilder.build().postForObject(new URI("http://localhost:8082/demo3"), tweet, String.class);
//        }
    }

    public static class Tweet {
        public String text;
    }
}
