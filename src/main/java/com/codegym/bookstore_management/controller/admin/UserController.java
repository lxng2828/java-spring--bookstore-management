package com.codegym.bookstore_management.controller.admin;

import com.codegym.bookstore_management.model.User;
import com.codegym.bookstore_management.service.UserService;

import jakarta.validation.Valid;

import com.codegym.bookstore_management.service.RoleService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.Optional;
import com.codegym.bookstore_management.model.Role;
import com.codegym.bookstore_management.service.UploadService;
import org.springframework.security.crypto.password.PasswordEncoder;

@Controller
@RequestMapping("/admin/users")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final UploadService uploadService;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, RoleService roleService, UploadService uploadService,
            PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.uploadService = uploadService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping
    public String listUsers(Model model) {
        model.addAttribute("users", userService.findAllUsers());
        return "admin/user/list";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("roles", roleService.findAll());
        return "admin/user/form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Optional<User> userOpt = userService.findUserById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            model.addAttribute("roles", roleService.findAll());
            return "admin/user/form";
        } else {
            return "redirect:/admin/users";
        }
    }

    @PostMapping("/save")
    public String saveUser(@ModelAttribute("user") @Valid User user, BindingResult bindingResult,
            @RequestParam("role.id") Long roleId,
            @RequestParam(value = "avatarFile", required = false) MultipartFile avatarFile,
            Model model) {

        model.addAttribute("roles", roleService.findAll());

        if (bindingResult.hasErrors()) {
            return "admin/user/form";
        }

        Role role = roleService.findById(roleId).orElseThrow();
        user.setRole(role);

        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
        }

        if (avatarFile != null && !avatarFile.isEmpty()) {
            String avatarWebPath = uploadService.uploadFile(avatarFile, "avatar");
            if (avatarWebPath != null) {
                user.setAvatar(avatarWebPath);
            }
        }
        userService.saveUser(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/detail/{id}")
    public String viewUserDetail(@PathVariable Long id, Model model) {
        Optional<User> userOpt = userService.findUserById(id);
        if (userOpt.isPresent()) {
            model.addAttribute("user", userOpt.get());
            return "admin/user/detail";
        } else {
            return "redirect:/admin/users";
        }
    }

    @GetMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/admin/users";
    }
}