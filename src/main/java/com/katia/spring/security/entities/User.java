package com.katia.spring.security.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.*;
import java.util.stream.Collectors;


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "age")
    private String age;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "users_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))

    private Set<Role> roles;

    public List<String> getRoleNames() {
        return roles.stream()
                .map(Role::getRoleName)
                .collect(Collectors.toList());
    }


    public String getRolesAsString() {
        List<String> roleNames = getRoleNames();
        return String.join(" ", roleNames);
    }
}



//    public String getRolesAsString(Set<Role> roles) {
//        return roles.stream()
//                .map(Role::getRoleName)
//                .collect(Collectors.joining(" "));
//    }


//    public Set<Role> getRoles() {
//        return this.roles;
//    }
//
//    public List<String> getRoleList() {
//        List<String> roleNames = new ArrayList<>();
//        for (Role role : roles) {
//            roleNames.add(role.getRoleName());
//        }
//        return roleNames;
//    }

//    public String getRolesAsString() {
//        List<String> roleNames = getRoleList();
//        return String.join(" ", roleNames);
//    }



