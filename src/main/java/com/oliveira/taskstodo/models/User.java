package com.oliveira.taskstodo.models;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;
import com.oliveira.taskstodo.models.enums.ProfileEnum;

import jakarta.persistence.FetchType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity 
@Table(name = User.TABLE_NAME)
@AllArgsConstructor // All this lombok can be replace for @Date - little different
@NoArgsConstructor
@Data //@Getter @Setter @EqualsAndHashCode
public class User {
    
    public static final String TABLE_NAME = "user";

    //The recomendation is to use Integuer, not int
    //The primitive tipe (int) may couse an execute error or to null pointer   
    // Ando for id is betther use ass (Long)
    @Id
    @Column(name = "id", unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "username", length = 100, nullable = false, unique = true)
    @Size(min = 2, max = 100)
    @NotBlank
    private String username;
    

    // Password just to read
    // create the password and never retur it's to the front, 
    // never retur to the user
    @Column(name = "password", length = 60, nullable = false)
    @JsonProperty(access = Access.WRITE_ONLY)
    @Size(min = 8, max = 60)
    @NotBlank
    private String password; 


    // NEED REFACTORY
    // Can't add this list in the constructors, don't to use to do that
    //      -> bad performance
    @OneToMany(mappedBy = "user")
    @JsonProperty(access = Access.WRITE_ONLY)
    private List<Task> tasks = new ArrayList<Task>();

    //can be user and admin
    //set can't permit repeat values
    @Column(name = "profile", nullable = false)
    @ElementCollection(fetch = FetchType.EAGER) // ever get profile when get user
    @CollectionTable(name = "user_profile")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY) // dont's return to user his profiles
    private Set<Integer> profiles = new HashSet<>();
    
    // transform Integer in set profile of enum
    public Set<ProfileEnum> getProfiles(){
        return this.profiles.stream().map(
            x -> ProfileEnum.toEnum(x)).collect(Collectors.toSet());
    }

    public void addProfile(ProfileEnum profileEnum) {
        this.profiles.add(profileEnum.getCode());
    }

}
