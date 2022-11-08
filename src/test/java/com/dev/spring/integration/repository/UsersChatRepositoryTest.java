package com.dev.spring.integration.repository;

import com.dev.spring.database.entity.UserChat;
import com.dev.spring.database.repository.UsersChatRepository;
import com.dev.spring.integration.IntegrationTestBase;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.List;

@RequiredArgsConstructor
class UsersChatRepositoryTest extends IntegrationTestBase {

    private final UsersChatRepository usersChatRepository;

    @Test
    void findAllByUserId() {
        List<UserChat> id = usersChatRepository.findAllByUserId(1L);
        System.out.println(id);
    }
}