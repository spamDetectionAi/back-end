package com.tsix_hack.spam_ai_detection.entities.MessagesMango.SenderManager;

import com.fasterxml.jackson.databind.jsontype.NamedType;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JacksonConfig {
    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jacksonCustomizer() {
        return builder -> {
            SimpleModule module = new SimpleModule();
            module.registerSubtypes(
                    new NamedType(ForeignSender.class, "email"),
                    new NamedType(LocalSender.class, "senderId")
            );
            builder.modules(module);
        };
    }
}
