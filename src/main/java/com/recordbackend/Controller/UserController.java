package com.recordbackend.Controller;

import com.recordbackend.Dto.UserDto;
import com.recordbackend.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    //get all users from the database
    @GetMapping("/all")
    public List<UserDto> getAllUsers(){
        return userService.getAllUsers();
    }

    // get user by id
    @GetMapping("/{id}")
    public UserDto getUserDtoById(@PathVariable Long id){
        return userService.getUserDtoById(id);
    }

    // get user by email
    @GetMapping("/email/{email}")
    public UserDto getUserByEmail(@PathVariable String email){
        return userService.getUserByEmail(email);
    }

    // Update user by id
    @PutMapping("/update/{id}")
    public UserDto updateUser(@PathVariable Long id, @RequestBody UserDto userDto){
        return userService.updateUserById(id, userDto);
    }
    // delete user by id
    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable Long id){
        userService.deleteUserById(id);
    }
}
