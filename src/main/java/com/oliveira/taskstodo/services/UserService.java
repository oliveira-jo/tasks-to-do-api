package com.oliveira.taskstodo.services;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.oliveira.taskstodo.models.User;
import com.oliveira.taskstodo.models.enums.ProfileEnum;
import com.oliveira.taskstodo.repositories.UserRepository;
import com.oliveira.taskstodo.services.exceptions.DataBindingViolationException;
import com.oliveira.taskstodo.services.exceptions.ObjectNotFoundException;

import jakarta.transaction.Transactional;

@Service
public class UserService {
    
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private UserRepository userRepository;


    public User findById(Long id){

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
        //encrypt password
        obj.setPassword(this.bCryptPasswordEncoder.encode(obj.getPassword()));
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
        //encrypt password
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

}
