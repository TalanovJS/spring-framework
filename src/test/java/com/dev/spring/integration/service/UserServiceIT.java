package com.dev.spring.integration.service;

import com.dev.spring.database.entity.Role;
import com.dev.spring.dto.UserCreateEditDto;
import com.dev.spring.dto.UserReadDto;
import com.dev.spring.integration.IntegrationTestBase;
import com.dev.spring.service.UserService;
import com.dev.spring.database.pool.ConnectionPool;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@RequiredArgsConstructor
class UserServiceIT extends IntegrationTestBase {

    private final UserService userService;
    private static final Long USER_ID = 1L;
    private static final Integer COMPANY = 1;

    @Test
    void findAll() {
        List<UserReadDto> result = userService.findAll();
        assertThat(result).hasSize(5);
    }

    @Test
    void findByID() {
        Optional<UserReadDto> maybeUser = userService.findById(USER_ID);
        assertTrue(maybeUser.isPresent());
        maybeUser.ifPresent(user -> assertEquals("ivan@gmail.com", user.getUsername()));
    }

    @Test
    void create() {
        UserCreateEditDto userDto = new UserCreateEditDto(
                "terror3x@gmail.com",
                "123",
                LocalDate.of(1990, 5, 3),
                "Roman",
                "Talanov",
                Role.ADMIN,
                COMPANY,
                new MockMultipartFile("test", new byte[0])
        );

        UserReadDto actualResult = userService.create(userDto);

        assertEquals(actualResult.getUsername(), userDto.getUsername());
        assertEquals(actualResult.getBirthDate(), userDto.getBirthDate());
        assertEquals(actualResult.getFirstname(), userDto.getFirstname());
        assertEquals(actualResult.getLastname(), userDto.getLastname());
        assertEquals(actualResult.getRole(), userDto.getRole());
        assertSame(actualResult.getCompany().getId(), userDto.getCompanyId());
    }

    @Test
    void update() {
        UserCreateEditDto userDto = new UserCreateEditDto(
                "terror31x@gmail.com",
                "1234",
                LocalDate.of(1990, 5, 3),
                "Roman",
                "Talanov",
                Role.ADMIN,
                COMPANY,
                new MockMultipartFile("test", new byte[0])
        );

        Optional<UserReadDto> actualResult = userService.update(USER_ID, userDto);
        assertTrue(actualResult.isPresent());
        actualResult.ifPresent(user -> {
            assertEquals(userDto.getUsername(), user.getUsername());
            assertEquals(userDto.getBirthDate(), user.getBirthDate());
            assertEquals(userDto.getFirstname(), user.getFirstname());
            assertEquals(userDto.getLastname(), user.getLastname());
            assertEquals(userDto.getRole(), user.getRole());
            assertSame(userDto.getCompanyId(), user.getCompany().getId());
        });
    }

    @Test
    void delete() {
        assertFalse(userService.delete(12L));
        assertTrue(userService.delete(USER_ID));
    }

}