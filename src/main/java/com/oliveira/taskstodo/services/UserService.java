package com.oliveira.taskstodo.services;

import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.oliveira.taskstodo.models.User;
import com.oliveira.taskstodo.models.dto.UserCreateDTO;
import com.oliveira.taskstodo.models.dto.UserUpdateDTO;
import com.oliveira.taskstodo.models.enums.ProfileEnum;
import com.oliveira.taskstodo.repositories.UserRepository;
import com.oliveira.taskstodo.security.UserSpringSecurity;
import com.oliveira.taskstodo.services.exceptions.AuthorizationException;
import com.oliveira.taskstodo.services.exceptions.DataBindingViolationException;
import com.oliveira.taskstodo.services.exceptions.ObjectNotFoundException;

import jakarta.validation.Valid;


@Service
public class UserService {
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    public User findById(Long id){

        // There are anyone logged / is admin / have the same id
        UserSpringSecurity userSpringSecurity = authenticated();
        if ( !Objects.nonNull(userSpringSecurity)              
                || !userSpringSecurity.hasRole(ProfileEnum.ADMIN) 
                && !id.equals(userSpringSecurity.getId()))          
            throw new AuthorizationException("Acesso negado!");

        //return the user from database if existe or empy if not
        Optional<User> user = this.userRepository.findById(id);
        //in this case using an Arrow Funcion an fuincion inside the other

        // new Exception -> stop application and new RuntimeException -> just show without stop 
        return user.orElseThrow(() -> new ObjectNotFoundException( //exception created in exceptions
            "Usuário não encontrado! id: " + id + ", Tipo: " + User.class.getName()
        ));

    }

    //@Transactional - help to save every task in the database 
    // when save and update in the database, not put in the find couse 
    // turn heavy in the application
    @Transactional
    public User create(User obj){
        // if anyone send a obj with a new id, it will be reset the id in the Database
        // becouse this we need to reset the id here
        obj.setId(null);
        //save encrypt password
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        // code of user (ProfileEnum.USER.getCode())
        // guarantee to save the type of user 
        obj.setProfiles(Stream.of(ProfileEnum.USER.getCode()).collect(Collectors.toSet()));
        this.userRepository.save(obj);
        return obj;
    }

    @Transactional
    public User update(User obj){
        //use the funcion that we have in this class
        User newObj = findById(obj.getId());
        // User can update only the password
        newObj.setPassword(obj.getPassword());
        //update encrypt password
        newObj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
        return this.userRepository.save(newObj);
    }

    public void delete(Long id){
        findById(id);
        // there are a chance to get a error if you delete a entity that have 
        // relation with another entity, for this we put a try-catch here
        //try {
        //    this.userRepository.deleteById(id);
        //} catch (Exception e) {
        //    throw new RuntimeErrorException(e, "Não é possivel excluir porque ha entidades relacionadas!");
        //}
        try {
            this.userRepository.deleteById(id);
        } catch (Exception e) {
            throw new DataBindingViolationException("Não é possivel excluir porque há entidades relacionadas!");
        }
    }

    //how is in the context in this moment, with have anybody authenticated
    public static UserSpringSecurity authenticated() {
        try {            
            return (UserSpringSecurity) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        } catch (AuthenticationException e) {
            return null;
        }
    }

    public User fromDTO(@Valid UserCreateDTO obj) {
        User user = new User();
        user.setUsername(obj.getUsername());
        user.setPassword(obj.getPassword());
        return user;
    }

    public User fromDTO(@Valid UserUpdateDTO obj) {
        User user = new User();
        user.setId(obj.getId());
        user.setPassword(obj.getPassword());
        return user;
    }

}
