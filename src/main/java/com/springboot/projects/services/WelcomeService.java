package com.springboot.projects.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
public class WelcomeService {
    @Value("${welcome.message}")
    private String message;
    public String retrieveWelcomeMessage(){
        return message;
    }

    @Profile("prod")
    @Bean
    public String createBeanInProd(){
        return "this is prod env";
    }
}
