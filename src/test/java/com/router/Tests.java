package com.router;

import com.entity.GoodEntity;
import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.junit5.CamelSpringBootTest;
import org.apache.camel.test.spring.junit5.MockEndpoints;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@CamelSpringBootTest
@EnableAutoConfiguration
@SpringBootTest(properties = {"kafka-requests-path=direct:requests"})
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@MockEndpoints
public class Tests {

    @Autowired
    ProducerTemplate producerTemplate;

    @EndpointInject("mock:jpa:com.entity.GoodEntity")
    public MockEndpoint saveToDb;

    @EndpointInject("mock:kafka:results")
    public MockEndpoint kafkaResults;

    @EndpointInject("mock:kafka:status_topic")
    public MockEndpoint kafkaStatusTopic;

    @Test
    public void saveToDBTest() throws InterruptedException, ParseException {
        GoodEntity good = new GoodEntity();
        good.setGood_name("cellphone");
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        good.setDelivery_date(new Date(dateFormat.parse("2023-05-29").getTime()));
        saveToDb.expectedBodiesReceived(good);

        producerTemplate.sendBody("direct:requests", "<good><name>cellphone</name>" +
                "<price>1075</price><delivery_date>2023-05-29</delivery_date></good>");

        saveToDb.assertIsSatisfied(5000);
    }

    @Test
    public void kafkaResultsTest() throws InterruptedException {
        kafkaResults.expectedBodiesReceived("{\"name\":\"cellphone\",\"delivery_date\":\"2023-05-29\"}");

        producerTemplate.sendBody("direct:requests", "<good><name>cellphone</name>" +
                "<price>1075</price><delivery_date>2023-05-29</delivery_date></good>");

        MockEndpoint.assertIsSatisfied(kafkaResults);
    }

    @Test
    public void sendOKStatusTest() throws InterruptedException {
        kafkaStatusTopic.expectedBodiesReceived("<status>ok</status>");

        producerTemplate.sendBody("direct:requests", "<good><name>cellphone</name>" +
                "<price>1075</price><delivery_date>2023-05-29</delivery_date></good>");

        kafkaStatusTopic.assertIsSatisfied(5000);
    }

    @Test
    public void sendErrorStatusTest() throws InterruptedException {
        kafkaStatusTopic.expectedBodiesReceived("<status>error</status><message>Unmarshaling failed</message>");

        producerTemplate.sendBody("direct:requests", "<not_good><name>cellphone</name>" +
                "<price>1075</price><delivery_date>2023-05-29</delivery_date></good>");

        kafkaStatusTopic.assertIsSatisfied(5000);
    }
}
