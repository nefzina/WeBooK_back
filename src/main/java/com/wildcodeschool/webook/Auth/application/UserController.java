package com.wildcodeschool.webook.Auth.application;

import com.wildcodeschool.webook.Auth.domain.dto.PasswordDTO;
import com.wildcodeschool.webook.Auth.domain.dto.UserDTO;
import com.wildcodeschool.webook.Auth.domain.entity.User;
import com.wildcodeschool.webook.Auth.domain.service.UserMapper;
import com.wildcodeschool.webook.Auth.domain.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.function.ServerResponse;

import java.util.Collections;
import java.util.List;


@RestController
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserDTO> readAll() {
        return userService.getAllUsers();
    }

    @GetMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public ResponseEntity<?> readOne(@PathVariable Long id) {
        return ResponseEntity.status(200).body(userService.getOneUser(id));
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO create(@RequestBody User newUser) {
        return userService.createUser(newUser);
    }

    @PutMapping(value = "/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserDTO edit(@RequestBody User newUser, @PathVariable Long id) {
        return userService.updateUser(newUser, id);
    }
    @PatchMapping(value = "/users/{id}/pw")
    public HttpStatus editPassword(@RequestBody PasswordDTO passwordDTO, @PathVariable Long id) {
        return userService.updatePassword(passwordDTO, id);
    }

    @DeleteMapping("/users/{id}")
    public void delete(@PathVariable Long id) {
        userService.deleteUser(id);
    }
}
