package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import com.appsdeveloperblog.tutorials.junit.service.UsersService;
import com.appsdeveloperblog.tutorials.junit.service.UsersServiceImpl;
import com.appsdeveloperblog.tutorials.junit.shared.UserDto;
import com.appsdeveloperblog.tutorials.junit.ui.request.UserDetailsRequestModel;
import com.appsdeveloperblog.tutorials.junit.ui.response.UserRest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = UsersController.class, excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false;
@MockBean({UsersServiceImpl.class})
public class UsersControllerWebLayerTest {

    @Autowired
    private MockMvc mockMvc;

//    @MockBean
    @Autowired
    UsersService usersService;

    UserDetailsRequestModel userDetailsRequestModel = new UserDetailsRequestModel();

    @BeforeEach
    void setup() {
        userDetailsRequestModel.setFirstName("");
        userDetailsRequestModel.setLastName("Corcoran");
        userDetailsRequestModel.setEmail("robbie@robbie.com");
        userDetailsRequestModel.setPassword("123456789");
    }

    @Test
    @DisplayName("User can be created.")
    void testCreateUser_whenValidUserDetailsProvided_returnsCreatedUserDetails() throws Exception {
//        Arrange
        UserDetailsRequestModel userDetailsRequestModel = new UserDetailsRequestModel();
        userDetailsRequestModel.setFirstName("Robbie");
        userDetailsRequestModel.setLastName("Corcoran");
        userDetailsRequestModel.setEmail("robbie@robbie.com");
        userDetailsRequestModel.setPassword("123456789");

        UserDto userDto = new ModelMapper().map(userDetailsRequestModel, UserDto.class);
        userDto.setUserId(UUID.randomUUID().toString());
        when(usersService.createUser(any(UserDto.class))).thenReturn(userDto);

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

    @Test
    @DisplayName("First name is not empty.")
    void testCreateUser_whenFirstNameIsNotProvided_throws400BadRequest() throws Exception {
        //        Arrange
        userDetailsRequestModel.setFirstName("");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));


//        Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

//        Assert
        assertEquals(
                HttpStatus.BAD_REQUEST.value(),
                mvcResult.getResponse().getStatus(),
                "Incorrect HTTP Status Code returned"
        );
    }

    @Test
    @DisplayName("First name is longer than 2 characters.")
    void testCreateUser_whenFirstNameIsLessThanTwoCharacters_throws400BadRequest() throws Exception {
        //        Arrange
        userDetailsRequestModel.setFirstName("A");

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(userDetailsRequestModel));

//        Act
        MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();

//        Assert
        assertEquals(
                HttpStatus.BAD_REQUEST.value(),
                mvcResult.getResponse().getStatus(),
                "Incorrect HTTP Status Code returned"
        );
    }
}
