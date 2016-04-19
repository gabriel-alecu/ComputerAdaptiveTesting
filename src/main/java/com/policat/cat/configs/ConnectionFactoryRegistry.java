package com.policat.cat.configs;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.social.facebook.connect.FacebookConnectionFactory;
import org.springframework.stereotype.Component;
import org.thymeleaf.expression.Lists;


/**
 * Created by melany on 4/14/2016.
 */

@Component
public class ConnectionFactoryRegistry {

    private String apiKey, apiSecret;

    public ConnectionFactoryRegistry() {
    }


    public ConnectionFactoryRegistry(@Value("app.config.oauth.facebook.apikey") final String apiKey,
                                     @Value("app.config.oauth.facebook.apisecret") final String apiSecret){
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }



}
