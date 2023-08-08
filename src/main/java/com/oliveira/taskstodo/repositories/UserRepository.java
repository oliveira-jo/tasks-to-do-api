package com.oliveira.taskstodo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
import com.oliveira.taskstodo.models.User;

import org.springframework.transaction.annotation.Transactional;


// In this case, Spring Boot will be able to create the repository automatically
// from auto configuration
//@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @Transactional(readOnly = true)
    User findByUsername(String username);
}
