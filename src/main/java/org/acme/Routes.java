package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class Routes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("telegram:bots")
                .log("${body}")
                .transform(constant("Merci pour votre message!"))
                .to("telegram:bots");

    }
}
