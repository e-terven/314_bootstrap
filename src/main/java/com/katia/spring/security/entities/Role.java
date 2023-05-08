package com.katia.spring.security.entities;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;


@Entity(name = "roles")
@NoArgsConstructor
@Setter
@Getter
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    private String roleName;

    @ManyToMany(mappedBy = "roles")
    private Set<User> roles;

//    private List<String> roleNames;

    @Override
    public String getAuthority() {
        return getRoleName();
    }


    public static Role fromString(String roleName) {
        return new Role();
    }
}
