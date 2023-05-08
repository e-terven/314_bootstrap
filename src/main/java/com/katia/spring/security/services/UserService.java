package com.katia.spring.security.services;

import com.katia.spring.security.entities.Role;
import com.katia.spring.security.model.AuthenticationRequest;
import com.katia.spring.security.entities.User;
import com.katia.spring.security.model.UserDTO;
import com.katia.spring.security.repositories.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
@Transactional
public class UserService {
    private final UserRepository userRepository;
    private final RoleService roleService;

    @Autowired
    public UserService(UserRepository userRepository,RoleService roleService) {
        this.userRepository = userRepository;
        this.roleService = roleService;
    }

    public List<User> findAll() {return userRepository.findAll();}
    public User findById(Long id) {
        return userRepository.getOne(id);
    }
    public User findByEmail(String email) {return userRepository.findByEmail(email);
    }

    // --------------------------CREATE----------------------------------------

    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    public void createUser(UserDTO newUser, Set<Role> roles) {
        User user = new User();
        BeanUtils.copyProperties(newUser, user);
        newUser.setRoles(roles);
        newUser.setRoleNames(newUser.getRoleNames());
        userRepository.save(user);
    }

    // --------------------------UPDATE----------------------------------------


    public boolean isEmailTakenByOtherUser(String email, Long id) {
        return userRepository.existsByEmailAndIdNot(email, id);
    }
//    public void updateUser(Long id, UserDTO userUpdateDto, Set<Role> roles) {
//        Optional<User> existingUser = userRepository.findById(id);
//        if (existingUser.isPresent()) {
//            User user = existingUser.get();
//            user.setId(userUpdateDto.getId());
//            user.setEmail(userUpdateDto.getEmail());
//            user.setFirstName(userUpdateDto.getFirstName());
//            user.setLastName(userUpdateDto.getLastName());
//            user.setAge(userUpdateDto.getAge());
//            user.setPassword(userUpdateDto.getPassword());
//
//            user.setRoles(roles);
//            user.setRoleNames(userUpdateDto.getRoleNames());
//
////            user.setRoleNames(userUpdateDto.getRoleNames());
////            user.setRoles(userUpdateDto.getRoles());
//
//            userRepository.save(user);
//        }
//    }

    public void updateUser(Long userId, UserDTO updatedUser, Set<Role> roles) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            BeanUtils.copyProperties(updatedUser, user, "id"); // копируем все свойства кроме id
            updatedUser.setRoles(roles);
            updatedUser.setRoleNames(updatedUser.getRoleNames());
            userRepository.save(user);
        }
    }

    // --------------------------DELETE----------------------------------------

    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    // --------------------------SuccessUserHandler----------------------------------------

    public Long getCurrentUserId(Authentication authentication) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email);
        return user.getId();
    }
}
