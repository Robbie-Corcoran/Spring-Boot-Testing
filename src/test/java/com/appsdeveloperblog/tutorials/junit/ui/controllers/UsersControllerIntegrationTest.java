package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(
//        locations = "/application-test.properties",
//        properties = "server.port=8081")
public class UsersControllerIntegrationTest {

    @Value("${server.port}")
    private int serverPort;

    @LocalServerPort
    private int localSeverPort;

    @Test
    @DisplayName("User can be created.")
    void testCreateUser_whenValidDetailsProvided_returnUserDetails() throws JSONException {
//        Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Robbie");
        userDetailsRequestJson.put("lastName", "Corcoran");
        userDetailsRequestJson.put("email", "robbie@robbie.com");
        userDetailsRequestJson.put("password", "123456789");
        userDetailsRequestJson.put("repeatPassword", "123456789");


//        Act

//        Assert
    }

}
