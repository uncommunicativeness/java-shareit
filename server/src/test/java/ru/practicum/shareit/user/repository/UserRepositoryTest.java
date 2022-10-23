package ru.practicum.shareit.user.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.shareit.user.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class UserRepositoryTest {
    @Autowired
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void beforeEach() {
        user = userRepository.save(User.builder()
                .name("Name")
                .email("name@email.com")
                .build());

    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    @Test
    void findById() {
        User userFromDb = userRepository.findById(user.getId()).get();

        assertEquals(user.getId(), userFromDb.getId());
        assertEquals(user.getName(), userFromDb.getName());
        assertEquals(user.getEmail(), userFromDb.getEmail());
    }

    @Test
    void findAll() {
        List<User> users = userRepository.findAll();
        assertEquals(users.size(), 1);
        assertEquals(user.getId(), users.get(0).getId());
        assertEquals(user.getName(), users.get(0).getName());
        assertEquals(user.getEmail(), users.get(0).getEmail());
    }

    @Test
    void delete() {
        userRepository.deleteById(user.getId());
        assertEquals(userRepository.findAll().size(), 0);
    }
}