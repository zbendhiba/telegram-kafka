import org.apache.camel.builder.RouteBuilder;


from('timer:chuckNorrisTimer?period=5000&repeatCount=3') 
    .transform(simple('Message #${exchangeProperty.CamelTimerCounter}'))
    .to('log:mylogger')
    .to('knative:channel/jokes')
    
;
   