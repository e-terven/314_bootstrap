package com.katia.spring.security.controllers;

import com.katia.spring.security.entities.Role;
import com.katia.spring.security.entities.User;
import com.katia.spring.security.model.AuthenticationRequest;
import com.katia.spring.security.model.UserDTO;
import com.katia.spring.security.services.RoleService;
import com.katia.spring.security.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.*;

@Controller
public class MainController {
    private final UserService userService;
    private final RoleService roleService;

    @Autowired
    public MainController (UserService userService, RoleService roleService) {

        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/")
    public String getIndex() {
        return "index";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/logout")
    public String getLogoutPage() {
        return "login";
    }

    // --------------------------------- USER -------------------------------------------
    @GetMapping("/user/{id}")
    public String getUser(@PathVariable("id") Long id, Model model) {

        var user = userService.findById(id);
        String email = user.getEmail();
        String role = user.getRolesAsString();

        model.addAttribute("user", userService.findById(id));
        model.addAttribute("email", email);
        model.addAttribute("role", role);

        return "show";
    }

    // --------------------------------- TABLE -------------------------------------------
    @GetMapping("/admin/users")
    public String findAll(Model model, Authentication authentication){

        model.addAttribute("users", userService.findAll());
        String email = authentication.getName();
        String role = authentication.getAuthorities()
                .stream()
                .findFirst()
                .orElse(null)
                .getAuthority();
        model.addAttribute("userForHeader", userService.findByEmail(email));
        model.addAttribute("role", role);
        return "users";
    }

// ---------------------------------UPDATE-------------------------------------------

    @PostMapping("/admin/edit/{id}")
    public String updateUser(@ModelAttribute("userUpdateDto") UserDTO user,
                             @PathVariable("id") Long id,
                             @RequestParam(value = "roles", required = true) List<String> roleIds,
                             RedirectAttributes redirectAttributes) {

        String email = user.getEmail();
        if (email !=null && userService.isEmailTakenByOtherUser(email, id)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "User with email: " + email + " already exists. Try again.");
            redirectAttributes.addAttribute("id", id);
            return "redirect:/admin/users";
        } else {
            Set<Role> roles = new HashSet<>();
            for (String roleId : roleIds) {
                roles.add(Role.fromString(roleId));
            }
            userService.updateUser(id, user, roles);
            return "redirect:/admin/users";
        }
    }

// ---------------------------------CREATE-------------------------------------------

    @PostMapping("/admin/new")
    public String createUser(UserDTO newUser,
                             @RequestParam(value = "roles", required = true) List<String> roleIds,
                             RedirectAttributes redirectAttributes) {

        String email = newUser.getEmail();
        if (userService.existsByEmail(email)) {
            redirectAttributes.addFlashAttribute("errorMessage",
                    "User with email: " + email + " already exists.");
            return "redirect:/admin/users";
        } else {
            Set<Role> roles = new HashSet<>();
            for (String roleId : roleIds) {
                roles.add(Role.fromString(roleId));
            }
            userService.createUser(newUser, roles);
            return "redirect:/admin/users";
        }
    }

// ---------------------------------DELETE-------------------------------------------
    @PostMapping("/admin/users/{id}")
    public String deleteUser(@PathVariable("id") Long id){
        userService.deleteById(id);
        return "redirect:/admin/users";
    }
}
