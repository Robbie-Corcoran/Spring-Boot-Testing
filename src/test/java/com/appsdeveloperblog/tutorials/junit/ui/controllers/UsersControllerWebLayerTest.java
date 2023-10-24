package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.UnsupportedEncodingException;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(controllers = UsersController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
public class UsersControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    @DisplayName("User can be created.")
    void testCreateUser_whenValidUserDetailsProvided_returnsCreatedUserDetails() throws Exception {
//        Arrange
        UserDetailsRequestModel userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("Robbie");
        userDetailsRequestModel.setLastName("Corcoran");
        userDetailsRequestModel.setEmail("robbie@robbie.com");
        userDetailsRequestModel.setPassword("123456789");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));


//        Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
        String responseBodyAsString = mvcResult.getResponse().getContentAsString();

        UserRest createdUser = new ObjectMapper().readValue(responseBodyAsString, UserRest.class);

//        Assert
        assertEquals(
                userDetailsRequestModel.getFirstName(),
                createdUser.getFirstName(),
                "The returned user's first name is incorrect."
        );

        assertEquals(
                userDetailsRequestModel.getLastName(),
                createdUser.getLastName(),
                "The returned user's last name is incorrect."
        );

        assertEquals(
                userDetailsRequestModel.getEmail(),
                createdUser.getEmail(),
                "The returned user's email is incorrect."
        );

        assertFalse(
                createdUser.getUserId().isEmpty(),
                "User ID is empty."
        );

    }
}
