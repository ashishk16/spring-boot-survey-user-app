package com.springboot.projects.controllers;

import com.springboot.projects.configuration.BasicConfiguration;
import com.springboot.projects.services.WelcomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {
    @Autowired
    private WelcomeService welcomeService;
    @Autowired
    private BasicConfiguration configuration;

    @RequestMapping("/welcome")
    public String welcome()
    {
        return welcomeService.retrieveWelcomeMessage();
    }


    @RequestMapping("/dynamic-configuration")
    public Map dynamicConfiguration() {
        // Not the best practice to use a map to store differnt types!
        Map map = new HashMap();
        map.put("message", configuration.getMessage());
        map.put("number", configuration.getNumber());
        map.put("key", configuration.isValue());
        return map;
    }
}
