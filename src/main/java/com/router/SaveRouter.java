package com.router;

import com.dto.GoodDTO;
import com.entity.GoodEntity;
import com.generated.Good;
import com.mapper.GoodMapper;
import lombok.RequiredArgsConstructor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SaveRouter extends RouteBuilder {
    private final GoodMapper mapper;

    @Override
    public void configure() {
        from("direct:save_to_db")
                .process(exchange -> {
                    Good in = exchange.getIn().getBody(Good.class);
                    GoodEntity good = mapper.mapGenerated(in);

                    exchange.getMessage().setBody(good, GoodEntity.class);
                })
                .log("Saving ${body} to database")
                .to("jpa:com.entity.GoodEntity")
                .process(exchange -> {
                    GoodEntity in = exchange.getIn().getBody(GoodEntity.class);
                    GoodDTO good = mapper.mapWithoutId(in);

                    exchange.getMessage().setBody(good, GoodDTO.class);
                })
                .marshal().json(JsonLibrary.Jackson)
                .log("Sending ${body} to kafka")
                .to("kafka:results?brokers=localhost:9092")
                .setBody(simple("<status>ok</status>"))
                .to("direct:status")
                .to("direct:metrics_router_increment_success_messages")
                .to("direct:metrics_router_stop_timer");
    }
}
