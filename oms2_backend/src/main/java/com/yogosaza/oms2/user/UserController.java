package com.yogosaza.oms2.user;

import jakarta.persistence.Entity;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<?> create (@RequestBody UserRequestDto dto) {
        userService.create(dto);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUserByLoginId(@RequestParam String loginId) {
        UserResponseDto dto = userService.findByLoginId(loginId);
        return ResponseEntity.ok(dto);
    }



}
