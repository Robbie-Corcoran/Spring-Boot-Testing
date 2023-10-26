package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import javax.persistence.PersistenceException;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class UserEntityIntegrationTest {

    @Autowired
    private TestEntityManager testEntityManager;
    UserEntity userEntity;

    @BeforeEach
    void setup() {
        userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Robbie");
        userEntity.setLastName("Corcoran");
        userEntity.setEmail("test@test.com");
        userEntity.setEncryptedPassword("123456789");
    }

    @Test
    @DisplayName("UserEntity is created")
    void testUserEntity_whenValidUserDetailsProvided_shouldReturnStoredUserDetails() {
//        Act
        UserEntity storedUserEntity = testEntityManager.persistAndFlush(userEntity);

//        Assert
        assertTrue(
                storedUserEntity.getId() > 0,
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

    @Test
    @DisplayName("Length of firstName")
    void testUserEntity_whenFirstnameIsTooLong_shouldThrowException() {
//        Arrange
        userEntity.setFirstName("Qwertyuiopasdfghjklzxcvbnmqwertyuiopasdfghjklzxcvbnmwertyuiopasdfghjklzxcvbnm");

//        Act & Assert

        assertThrows(
                PersistenceException.class,
                () -> {
                    testEntityManager.persistAndFlush(userEntity);
                },
                "Was expecting a PersistenceException to be thrown.");
    }

    @Test
    @DisplayName("UserId must be unique.")
    void testUserEntity_whenUserIdIsNotUnique_shouldThrowException() {
//        Arrange
        UserEntity duplicateUserEntity = new UserEntity();
        duplicateUserEntity.setUserId(userEntity.getUserId());
        duplicateUserEntity.setFirstName("Robbie");
        duplicateUserEntity.setLastName("Corcoran");
        duplicateUserEntity.setEmail("test@test.com");
        duplicateUserEntity.setEncryptedPassword("123456789");

        testEntityManager.persistAndFlush(duplicateUserEntity);

//        Act & Assert
        assertThrows(
                PersistenceException.class,
                () -> {
                    testEntityManager.persistAndFlush(userEntity);
                },
                "Was expecting a PersistenceException to be thrown.");

    }
}
