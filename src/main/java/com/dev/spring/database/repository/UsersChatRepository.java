package com.dev.spring.database.repository;

import com.dev.spring.database.entity.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UsersChatRepository extends JpaRepository<UserChat, Long> {

    @Query(value = "select u from UserChat u join fetch u.user where u.id = :id")
    List<UserChat> findAllByUserId(Long id);
}
