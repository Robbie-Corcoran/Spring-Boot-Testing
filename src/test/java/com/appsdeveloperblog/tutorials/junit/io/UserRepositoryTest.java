package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsersRepository usersRepository;

    UserEntity userEntity;
    UserEntity otherUserEntity;

    @BeforeEach
    void setup() {
        userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Robbie");
        userEntity.setLastName("Corcoran");
        userEntity.setEmail("robbie@test.com");
        userEntity.setEncryptedPassword("123456789");

        otherUserEntity = new UserEntity();
        otherUserEntity.setUserId(UUID.randomUUID().toString());
        otherUserEntity.setFirstName("John");
        otherUserEntity.setLastName("Winfrey");
        otherUserEntity.setEmail("john@test.com");
        otherUserEntity.setEncryptedPassword("123456789");
    }


    @Test
    void testFindByEmail_whenGivenCorrectEmail_returnsUserEntity() {
//        Arrange & Act
        testEntityManager.persistAndFlush(userEntity);

        UserEntity storedUser = usersRepository.findByEmail(userEntity.getEmail());

//        Assert
        assertNotNull(
                storedUser,
                "UserEntity should not be null");

        assertEquals(
                userEntity.getEmail(),
                storedUser.getEmail(),
                "Returned email address does not match expected value");
    }

    @Test
    void testFindByUserId_whenGivenValidUserId_returnUserEntity() {
//        Arrange & Act
        testEntityManager.persistAndFlush(userEntity);
        UserEntity storedUser = usersRepository.findByUserId(userEntity.getUserId());

//        Assert
        assertNotNull(
                storedUser,
                "UserEntity should not be null");

        assertEquals(
                userEntity.getUserId(),
                storedUser.getUserId(),
                "Returned userId does not match expected value");
    }
}
