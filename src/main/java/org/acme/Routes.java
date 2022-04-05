package org.acme;

import javax.enterprise.context.ApplicationScoped;

import org.apache.camel.builder.RouteBuilder;

@ApplicationScoped
public class Routes extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        from("telegram:bots?authorizationToken={{telegram-token-api}}")
                .to("atlasmap:atlasmap-mapping.adm")
                .choice()
                    .when(simple("${body} contains '/start'"))
                        .transform(simple("{{msg.bot.start}}"))
                    .when(simple("${body} contains '#msg'"))
                        .transform(simple("{{msg.bot.msg}}"))
                    .otherwise()
                        .transform(simple("{{msg.bot.otherwise}}"))
                .end()
                .to("telegram:bots?authorizationToken={{telegram-token-api}}");



    }
}
