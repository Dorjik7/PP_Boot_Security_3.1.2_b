package ru.kata.spring.boot_security.demo.controller;

import org.springframework.security.core.Authentication;

import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.*;

import ru.kata.spring.boot_security.demo.model.User;

import ru.kata.spring.boot_security.demo.service.UserService;

import ru.kata.spring.boot_security.demo.service.security.AccountDetails;

import java.util.Arrays;

@Controller
@RequestMapping("/")
public class AdminController {

    private UserService adminService;

    public AdminController(UserService adminService) {

        this.adminService = adminService;

    }

    @GetMapping("/")
    public String home() {

        if (adminService.listUsers().isEmpty())

            adminService.addUser(new User("admin", "admin", Arrays.asList("ROLE_ADMIN")));

        return "index";

    }

    @GetMapping("/user")
    public String showUser(Model model) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        AccountDetails accountDetails = (AccountDetails) authentication.getPrincipal();

        model.addAttribute("user", accountDetails.getUser());

        return "user";

    }

    @GetMapping("/admin")

    public String showUsers(Model model) {

        model.addAttribute("users", adminService.listUsers());

        return "admin";

    }

    @GetMapping("/admin/register")

    public String register(Model model) {

        User user = new User();

        model.addAttribute("user", user);

        return "register";

    }

    @PostMapping("/admin/register")

    public String inputUser(@ModelAttribute("user") User user) {

        adminService.addUser(user);

        return "redirect:/admin";

    }

    @GetMapping("/admin/{id}/edit")

    public String editUser(@PathVariable("id") Long id, Model model) {

        model.addAttribute("editable_user", adminService.getUser(id));

        return "edit";

    }


    @PatchMapping("/admin/{id}")
    public String edit(@ModelAttribute("editable_user") User user, @PathVariable("id") Long id) {

        adminService.editUser(id, user);
        return "redirect:/admin";

    }

    @DeleteMapping("/admin/{id}")

    public String delete(@PathVariable("id") Long id) {

        adminService.deleteUser(id);

        return "redirect:/admin";

    }
}
