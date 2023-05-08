package com.katia.spring.security.model;

import com.katia.spring.security.entities.Role;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticationRequest {

    private Long id;
    private String firstName;
    private String lastName;
    private String age;
    private String email;
    private String password;
    private Set<Role> roles;
    private List<String> roleNames;


}
