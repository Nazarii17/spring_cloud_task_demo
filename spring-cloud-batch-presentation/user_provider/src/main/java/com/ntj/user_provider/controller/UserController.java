package com.ntj.user_provider.controller;

import com.ntj.springcloudbatchcommon.dto.UserRecordV1;
import com.ntj.springcloudbatchcommon.dto.UserRecordV2;
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

    @GetMapping("/v1")
    public List<UserRecordV1> getAllAvailableUsersV1() {
        return userService.getAllAvailableUsersV1();
    }

    @GetMapping("/v2")
    public List<UserRecordV2> getAllAvailableUsers() {
        return userService.getAllAvailableUsersV2();
    }

    @GetMapping("/complete")
    public ResponseEntity<Void> markUsersAsCompleted()  {
        userService.markUsersAsCompleted();
        return ResponseEntity.ok().build();
    }
}
