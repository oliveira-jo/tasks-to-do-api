package com.oliveira.taskstodo.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

//Add notations to generete the data base configuration

@Entity //Create a table 
@Table(name = User.TABLE_NAME)
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", unique = true) // not repeat ids
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
    private List<Task> tasks = new ArrayList<Task>();

    public User(){

    }

    public User(Long id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
    
    // Ignore tasks when search gor user
    // to do only dates to processed from user
    @JsonIgnore 
    public List<Task> getTasks() {
        return this.tasks;
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }


    @Override
    public boolean equals(Object object) {
        if (object == this)
            return true;
        if (object == null)
            return false;
        if (!(object instanceof User))
            return false;
        User other = (User) object;
        if (this.id == null) // one ins null and other not, not the same obj
            if (other.id != null)
                return false;
            else if (!this.id.equals(other.id))
                return false;
        return Objects.equals(this.id, other.id) && 
                Objects.equals(this.username, other.username) && 
                Objects.equals(this.password, other.password);
                //compost verification in the return to see if is the same object
    }

    @Override
    public int hashCode() {
        final int prime = 31; //aleatory number
        int result = 1; // to generete hash code
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        //operador ternario vigente  ? Se : senão  -> recursiv here
        return result;
    }
    
}
