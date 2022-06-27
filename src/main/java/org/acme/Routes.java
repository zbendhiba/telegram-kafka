package org.acme;

import java.util.UUID;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Named;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.aws2.s3.AWS2S3Constants;
import org.apache.camel.processor.idempotent.kafka.KafkaIdempotentRepository;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Routes extends RouteBuilder {

    @ConfigProperty(name = "camel.component.kafka.brokers")
    String brokers;

    @Named("kafkaIdempotentRepository")
    KafkaIdempotentRepository kafkaIdempotentRepository() {
        return new KafkaIdempotentRepository("idempotent-topic", brokers);
    }

    public void configure() throws Exception {

        from("google-sheets-stream:{{google-sheets.spreadsheet-id}}?range=Sheet1!A1:D200&splitResults=true")
                .throttle(1).timePeriodMillis(1500)
                .idempotentConsumer(body())
                .messageIdRepositoryRef("kafkaIdempotentRepository")
                .choice()
                .when(simple("${body} !contains 'Horodateur'"))
                        .bean(GoogleService.class)
                        .marshal().json()
                        .to("kafka:test")
                 .end();

        from("kafka:test")
               // .log("Incoming message from Kafka topic test ${body}")
                .setHeader(AWS2S3Constants.KEY, simple(UUID.randomUUID().toString()))
                .log(String.format("Sending message with header :: ${header.%s}", AWS2S3Constants.KEY))
                .to("aws2-s3:{{aws-s3.bucket-name}}");

    }



}
