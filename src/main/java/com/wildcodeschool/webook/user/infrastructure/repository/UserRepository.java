package com.wildcodeschool.webook.user.infrastructure.repository;

import com.wildcodeschool.webook.user.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
