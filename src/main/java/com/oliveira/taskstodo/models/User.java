package com.oliveira.taskstodo.models;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity 
@Table(name = User.TABLE_NAME)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class User {

    // Business rule
    // it's not possible to update user after it's creation
    // no contract but in the creat the program needs to verify
    // the determined rule 
    // Validation User Create -> CreateUser.class
    public interface CreateUser{}
    // Validation User Update -> UpdateUser.class
    public interface UpdateUser{}

    public static final String TABLE_NAME = "user";


    //The recomendation is to use Integuer, not int
    //The primitive tipe (int) may couse an execute error or to null pointer   
    // Ando for id is betther use ass (Long)
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "username", length = 100, nullable = false, unique = true)
    @NotNull(groups = CreateUser.class)
    // In this case it's possible verificate just in Create User, becouse 
    // the name con't be update for the business rule 
    @NotEmpty(groups = CreateUser.class)
    @Size(groups = CreateUser.class, min = 2, max = 100)
    private String username;
    

    // Password just to read
    // create the password and never retur it's to the front, 
    // never retur to the user
    @JsonProperty(access = Access.WRITE_ONLY)
    @Column(name = "password", length = 60, nullable = false)
    @NotNull(groups = {CreateUser.class, UpdateUser.class})
    // In this case it's need to verificat when Create and when Update
    // Becouse it can be update password in both cases
    @NotEmpty(groups = {CreateUser.class, UpdateUser.class})
    @Size(groups = {CreateUser.class, UpdateUser.class}, min = 8, max = 60)
    private String password; 


    // NEED REFACTORY
    // Can't add this list in the constructors, don't to use to do that
    //      -> bad performance
    @OneToMany(mappedBy = "user")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();

    
}
