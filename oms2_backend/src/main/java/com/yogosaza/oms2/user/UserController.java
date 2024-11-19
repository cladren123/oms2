package com.yogosaza.oms2.user;

import com.yogosaza.oms2.jwt.JwtUtil;
import com.yogosaza.oms2.user.dto.UserLoginDto;
import com.yogosaza.oms2.user.dto.UserRequestDto;
import com.yogosaza.oms2.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    public ResponseEntity<?> create (@RequestBody UserRequestDto dto) {
        userService.create(dto);
        return ResponseEntity.ok("ok");
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginDto userLoginDto) {
            System.out.println("check1");
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userLoginDto.getLoginId(), userLoginDto.getPassword())
            );
            System.out.println("check2");
            String token = jwtUtil.generateToken(userLoginDto.getLoginId());
            System.out.println("check3");
            return ResponseEntity.ok(Map.of("token", token));

    }

    @GetMapping("/find")
    public ResponseEntity<?> findUserByLoginId(@RequestParam String loginId) {
        UserResponseDto dto = userService.findByLoginId(loginId);
        return ResponseEntity.ok(dto);
    }



}
