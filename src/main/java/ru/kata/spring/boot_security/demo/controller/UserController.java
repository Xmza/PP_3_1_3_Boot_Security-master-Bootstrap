package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.service.UserService;

import java.security.Principal;
import java.util.List;

@Controller
public class UserController {

    private final UserService userService;
    private final RoleRepository roleRepository;

    @Autowired
    public UserController(UserService userService, RoleRepository roleRepository) {
        this.userService = userService;
        this.roleRepository = roleRepository;
    }

    @GetMapping(value = "/admin")
    public String listUsers(Model model, Principal principal) {
        List<User> users = userService.findAll();
        model.addAttribute("allUsers", users);
        model.addAttribute("user", new User());

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("currentUser", currentUser);
        model.addAttribute("roles", roleRepository.findAll());
        return "all-users";
    }

    @GetMapping("/addUser")
    public String addUser(Model model, Principal principal) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", roleRepository.findAll());

        User currentUser = userService.findByEmail(principal.getName());
        model.addAttribute("currentUser", currentUser);
        return "add-user";
    }

    @PostMapping("/editUser")
    public String editUser(@RequestParam("id") Long id, Model model) {
        User user = userService.getUser(id);
        model.addAttribute("user", user);
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("allRoles", roles);
        return "add-user";
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") User user) {
        userService.saveUser(user);
        return "redirect:/admin";
    }

    @PostMapping("/deleteUser")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

    @GetMapping("/user")
    public String profile(Principal principal, Model model) {
        model.addAttribute("user", userService.findByEmail(principal.getName()));
        return "user";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }
}
