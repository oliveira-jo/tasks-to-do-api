package com.oliveira.taskstodo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
//import org.springframework.stereotype.Repository;
import com.oliveira.taskstodo.models.Task;
import com.oliveira.taskstodo.models.projection.TaskProjection;

// In this case, Spring Boot will be able to create the repository automatically
// from auto configuration
//@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    //search for user id
    //Spring mode
    //Optional<Task>, anything that is null, put ampy. Function from java 8
    List<TaskProjection> findByUser_Id(long id);

    // Another Spring form, JPQL -> mix SQL and Funcions java
    // same return List<Task> of last one
    //@Query(value = "SELECT t FROM t WHERE t.user.id = :id") // <- : (parameter) and t from task
    //List<Task> findByUser_Id(@Param("id")long id);

    //Pure SQL
    //@Query(value = "SELECT * FROM task t WHERE t.user.id = :id", nativeQuery = true) 
    //List<Task> findByUser_Id(@Param("id")long id);

}
