package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.annotation.Order;
import org.springframework.http.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(
//        locations = "/application-test.properties",
//        properties = "server.port=8081")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UsersControllerIntegrationTest {

    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    @DisplayName("User can be created.")
    @Order(1)
    void testCreateUser_whenValidDetailsProvided_returnUserDetails() throws JSONException {
//        Arrange
        JSONObject userDetailsRequestJson = new JSONObject();
        userDetailsRequestJson.put("firstName", "Robbie");
        userDetailsRequestJson.put("lastName", "Corcoran");
        userDetailsRequestJson.put("email", "robbie@robbie.com");
        userDetailsRequestJson.put("password", "123456789");
        userDetailsRequestJson.put("repeatPassword", "123456789");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));

        HttpEntity<String> request = new HttpEntity<>(userDetailsRequestJson.toString(), headers);

//        Act
        ResponseEntity<UserRest> createdUserDetailsEntity = testRestTemplate.postForEntity(
                "/users",
                request,
                UserRest.class);
        UserRest createdUserDetails = createdUserDetailsEntity.getBody();

//        Assert
        assertEquals(
                HttpStatus.OK,
                createdUserDetailsEntity.getStatusCode(),
                "createUser did should have returned a 200 status code.");
        assertEquals(userDetailsRequestJson.getString(
                        "firstName"),
                createdUserDetails.getFirstName(),
                "The User's first name is not correct.");
        assertFalse(
                createdUserDetails.getUserId().trim().isEmpty(),
                "User's ID should not be empty.");
    }

    @Test
    @DisplayName("GET /users requires JWT.")
    @Order(2)
    void testGetUsers_whenMissingJWT_returns403() {
//        Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.set("Accept", "application/json");

        HttpEntity<Object> requestEntity = new HttpEntity<>(null, headers);

//        Act
        ResponseEntity<List<UserRest>> response = testRestTemplate.exchange(
                "/users",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });

//        Assert
        assertEquals(
                HttpStatus.FORBIDDEN,
                response.getStatusCode(),
                "HttpStatusCode 403 should have been returned.");
    }

    @Test
    @DisplayName("/login works.")
    @Order(3)
    void testUserLogin_whenValidCredentialsProvided_returnsJWTinAuthorizationHeader() throws JSONException {
//        Arrange
        JSONObject loginCredentials = new JSONObject();
        loginCredentials.put("email", "robbie@robbie.com");
        loginCredentials.put("password", "123456789");

        HttpEntity<String> request = new HttpEntity<>(loginCredentials.toString());

//        Act
        ResponseEntity<Object> response = testRestTemplate.postForEntity("/users/login", request, null);

        // Assert
        assertEquals(
                HttpStatus.OK,
                response.getStatusCode(),
                "HTTP Status code should be 200");
        assertNotNull(
                response.getHeaders().getValuesAsList("UserID").get(0),
                "Response should contain UserID in a response header");
    }
}
