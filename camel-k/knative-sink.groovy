import org.apache.camel.builder.RouteBuilder;


from('timer:chuckNorrisTimer?period=5000&repeatCount=10') 
    .transform(constant("Hello world"))
    .to("knative:channel/jokes")
    .log('Generated Chuck Norris joke: ${body}')
;
   