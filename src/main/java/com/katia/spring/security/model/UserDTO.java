package com.katia.spring.security.model;

import com.katia.spring.security.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private String age;
    private String email;
    private String password;
    private Set<Role> roles;
    private List<String> roleNames;

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
