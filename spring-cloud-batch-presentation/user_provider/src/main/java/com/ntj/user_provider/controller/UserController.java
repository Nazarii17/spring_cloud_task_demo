package com.ntj.user_provider.controller;

import com.ntj.springcloudbatchcommon.dto.UserRecord;
import com.ntj.user_provider.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserRecord> getAllAvailableUsers() {
        return userService.getAllAvailableUsers();
    }

    @GetMapping("/complete")
    public ResponseEntity<Void> markUsersAsCompleted()  {
        userService.markUsersAsCompleted();
        return ResponseEntity.ok().build();
    }
}
