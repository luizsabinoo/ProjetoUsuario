package com.example.project_.controler;


import com.example.project_.dtos.UserDto;
import com.example.project_.models.UserModel;
import com.example.project_.repositories.UserRepository;
import com.example.project_.service.UserService;
import jakarta.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;


import java.util.*;

@RestController
@RequestMapping("/users")


public class UserController {

    @Autowired
    private com.example.project_.repositories.UserRepository userRepository;

    @Autowired
    private UserService userService;

    @GetMapping
    public ResponseEntity<List<UserModel>> getAllUsers() {
        List<UserModel> users = userRepository.findAll();
        if(!users.isEmpty()) {
            users.forEach(user -> {
                UUID id = user.getId();
                user.add(linkTo(methodOn(UserController.class).getUser(id)).withSelfRel());
            });
        }
        return ResponseEntity.status(HttpStatus.OK).body(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getUser(@PathVariable UUID id) {
        Optional<UserModel> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        user.get().add(linkTo(methodOn(UserController.class).getAllUsers()).withRel("User List"));
        return ResponseEntity.status(HttpStatus.OK).body(user.get());
    }



    @PostMapping
    public ResponseEntity<Object> saveUser(@RequestBody @Valid UserDto userDto) {
        try {
            UserModel savedUser = userService.saveUser(userDto).getBody();
            return ResponseEntity.status(HttpStatus.CREATED).body(savedUser);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<Object> deleteUser(@PathVariable UUID id, @RequestBody @Valid UserDto userDto) {
        Optional<UserModel> user = userRepository.findById(id);
        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
        userRepository.delete(user.get());
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully");

    }

    @PutMapping
    public ResponseEntity<Object> updateUser(@PathVariable UUID id, @RequestBody @Valid UserDto userDto) {
        Optional<UserModel> user = userRepository.findById(id);

        if(user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");

        }
        UserModel updatedUser = user.get();
        BeanUtils.copyProperties(userDto, updatedUser);
        return ResponseEntity.status(HttpStatus.OK).body(userRepository.save(updatedUser));
    }


}
