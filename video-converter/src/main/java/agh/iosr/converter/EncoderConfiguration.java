package agh.iosr.converter;

import it.sauronsoftware.jave.Encoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncoderConfiguration {

    @Bean
    public Encoder encoder(){
        return new Encoder();
    }

}
