package com.oliveira.taskstodo.services;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.oliveira.taskstodo.models.Task;
import com.oliveira.taskstodo.models.User;
import com.oliveira.taskstodo.models.enums.ProfileEnum;
import com.oliveira.taskstodo.models.projection.TaskProjection;
import com.oliveira.taskstodo.repositories.TaskRepository;
import com.oliveira.taskstodo.security.UserSpringSecurity;
import com.oliveira.taskstodo.services.exceptions.AuthorizationException;
import com.oliveira.taskstodo.services.exceptions.DataBindingViolationException;
import com.oliveira.taskstodo.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class TaskService {
    
    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UserService userService;

    public Task findById(Long id){
        Task task = this.taskRepository.findById(id).orElseThrow(() -> new ObjectNotFoundException(
            "Tarefa não encontrada! id: " + id + ", Tipo: " + Task.class.getName()));
        
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity) 
                || ! userSpringSecurity.hasRole(ProfileEnum.ADMIN) 
                && ! userHasTask(userSpringSecurity, task))
            throw new AuthorizationException("Acesso negado!");

        return task;
    }

    //find all Tasks of one user
    public List<TaskProjection> findAllByUser(){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity))
            throw new AuthorizationException("Acesso negado!");

        List<TaskProjection> tasks = this.taskRepository.findByUser_Id(userSpringSecurity.getId());
        return tasks;
    }

    @Transactional
    public Task create(Task obj){
        UserSpringSecurity userSpringSecurity = UserService.authenticated();
        if (Objects.isNull(userSpringSecurity)) 
            throw new AuthorizationException("Acesso negado!");

        //get user for user logged    
        User user = this.userService.findById(userSpringSecurity.getId());
        obj.setId(null);
        obj.setUser(user);
        obj = this.taskRepository.save(obj);
        return obj; 
    }

    @Transactional
    public Task update(Task obj){
        //Checks user for the findById
        Task newObj = findById(obj.getId()); 
        newObj.setDescription(obj.getDescription());
        return this.taskRepository.save(newObj);
    }

    public void delete(Long id){
        //Checks user for the findById
        findById(id);
        try {
            this.taskRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possivel excluir pois há entidades relacionadas!");
        }
    }

    private Boolean userHasTask(UserSpringSecurity userSpringSecurity, Task task){
        return task.getUser().getId().equals(userSpringSecurity.getId());

    }

}
