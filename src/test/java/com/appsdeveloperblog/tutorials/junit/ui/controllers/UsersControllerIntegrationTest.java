package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestPropertySource(
        locations = "/application-test.properties",
        properties = "server.port=8081")
public class UsersControllerIntegrationTest {

    @Value("${server.port}")
    private int serverPort;
    @Test
    void test() {
        System.out.println("Server.port= " + serverPort);
    }

}
