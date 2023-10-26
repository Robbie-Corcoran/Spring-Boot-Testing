package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserEntityIntegrationTest {

    @Autowired
    TestEntityManager testEntityManager;
    @Autowired
    private UsersRepository usersRepository;

    @Test
    @DisplayName("UserEntity is created")
    void testUserEntity_whenValidUserDetailsProvided_shouldReturnStoredUserDetails() {
//        Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Robbie");
        userEntity.setLastName("Corcoran");
        userEntity.setEmail("test@test.com");
        userEntity.setEncryptedPassword("123456789");


//        Act
        UserEntity storedUserEntity = testEntityManager.persistAndFlush(userEntity);

//        Assert
        assertTrue(
                storedUserEntity.getId() == 1,
                "Should have only 1 UserId in table."
        );

        assertEquals(
                userEntity.getFirstName(),
                storedUserEntity.getFirstName(),
                "Returned UserFirstName is incorrect.");

        assertEquals(
                userEntity.getLastName(),
                storedUserEntity.getLastName(),
                "Returned UserLastName is incorrect");

        assertEquals(
                userEntity.getEmail(),
                storedUserEntity.getEmail(),
                "Returned UserEmail is incorrect");

        assertEquals(
                userEntity.getEncryptedPassword(),
                storedUserEntity.getEncryptedPassword(),
                "Returned UserEncryptedPassword is incorrect");

    }

}
