package com.appsdeveloperblog.tutorials.junit.io;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    TestEntityManager testEntityManager;

    @Autowired
    UsersRepository usersRepository;

    UserEntity userEntity1;
    UserEntity userEntity2;
    UserEntity userEntity3;

    @BeforeEach
    void setup() {
        userEntity1 = new UserEntity();
        userEntity1.setUserId(UUID.randomUUID().toString());
        userEntity1.setFirstName("Robbie");
        userEntity1.setLastName("Corcoran");
        userEntity1.setEmail("robbie@test.com");
        userEntity1.setEncryptedPassword("123456789");

        userEntity2 = new UserEntity();
        userEntity2.setUserId(UUID.randomUUID().toString());
        userEntity2.setFirstName("John");
        userEntity2.setLastName("Winfrey");
        userEntity2.setEmail("john@test.com");
        userEntity2.setEncryptedPassword("123456789");

        userEntity3 = new UserEntity();
        userEntity3.setUserId(UUID.randomUUID().toString());
        userEntity3.setFirstName("Eimear");
        userEntity3.setLastName("O'Reilly");
        userEntity3.setEmail("eimear@demo.com");
        userEntity3.setEncryptedPassword("123456789");
    }


    @Test
    void testFindByEmail_whenGivenCorrectEmail_returnsUserEntity() {
//        Arrange & Act
        testEntityManager.persistAndFlush(userEntity1);

        UserEntity storedUser = usersRepository.findByEmail(userEntity1.getEmail());

//        Assert
        assertNotNull(
                storedUser,
                "UserEntity should not be null");

        assertEquals(
                userEntity1.getEmail(),
                storedUser.getEmail(),
                "Returned email address does not match expected value");
    }

    @Test
    void testFindByUserId_whenGivenValidUserId_returnUserEntity() {
//        Arrange & Act
        testEntityManager.persistAndFlush(userEntity1);
        UserEntity storedUser = usersRepository.findByUserId(userEntity1.getUserId());

//        Assert
        assertNotNull(
                storedUser,
                "UserEntity should not be null");

        assertEquals(
                userEntity1.getUserId(),
                storedUser.getUserId(),
                "Returned userId does not match expected value.");
    }


    @Test
    void testFindUsersWithEmailEndingWith_whenGivenEmailDomain_returnAnyUserEntityWithGivenDomain() {
//        Arrange
        testEntityManager.persistAndFlush(userEntity1);
        testEntityManager.persistAndFlush(userEntity2);
        testEntityManager.persistAndFlush(userEntity3);

        String testEmailDomainName = "@test.com";
        String demoEmailDomainName = "@demo.com";


//        Act
        List<UserEntity> foundTestUsers = usersRepository.findUsersWithEmailEndingWith(testEmailDomainName);
        List<UserEntity> foundDemoUsers = usersRepository.findUsersWithEmailEndingWith(demoEmailDomainName);


//        Assert
        assertEquals(
                2,
                foundTestUsers.size(),
                "Returned list should contain 2 test UserEntities.");

        assertEquals(
                1,
                foundDemoUsers.size(),
                "Returned list should contain 1 demo UserEntity.");
    }
}
