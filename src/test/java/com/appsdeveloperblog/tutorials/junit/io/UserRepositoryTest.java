package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsersRepository usersRepository;

    @Test
    void testFindByEmail_whenGivenCorrectEmail_returnsUserEntity() {
//        Arrange
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId(UUID.randomUUID().toString());
        userEntity.setFirstName("Robbie");
        userEntity.setLastName("Corcoran");
        userEntity.setEmail("test@test.com");
        userEntity.setEncryptedPassword("123456789");

        testEntityManager.persistAndFlush(userEntity);

//        Act
        UserEntity storedUser = usersRepository.findByEmail(userEntity.getEmail());


//        Assert
        assertEquals(
                userEntity.getEmail(),
                storedUser.getEmail(),
                "Returned email address does not match expected value");
    }
}
