package com.policat.cat.services;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;


/**
 * Created by melany on 4/14/2016.
 */

@Service
public class FacebookUtil {

    @Value("${app.config.oauth.facebook.apikey}")
    private String apikey;

    //TODO: Cere permisiune pt everyone
    public MultiValueMap<String, Object> publishLinkWithVisiblityRestriction(
            String state) {
        MultiValueMap<String, Object> userRestrictedMap = new LinkedMultiValueMap<String, Object>();
        userRestrictedMap.set("privacy", "{value:\"EVERYONE\"}");
        userRestrictedMap.set("message",
                "My Post through Spring Social using Facebook Graph API");
        userRestrictedMap.set("picture", "");
        userRestrictedMap.set("caption", "ArpitAggarwal.in");
        return userRestrictedMap;
    }
}

