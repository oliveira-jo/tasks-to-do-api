package com.oliveira.taskstodo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.oliveira.taskstodo.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
