package com.appsdeveloperblog.tutorials.junit.ui.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@TestPropertySource(
//        locations = "/application-test.properties",
//        properties = "server.port=8081")
public class UsersControllerIntegrationTest {

//    @Value("${server.port}")
//    private int serverPort;

    @LocalServerPort
    private int localSeverPort;

    @Test
    void test() {
//        System.out.println("Server.port= " + serverPort);
        System.out.println("Local server port= " + localSeverPort);
    }

}
