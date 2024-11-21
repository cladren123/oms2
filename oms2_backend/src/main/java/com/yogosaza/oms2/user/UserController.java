package com.yogosaza.oms2.user;

import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.jwt.JwtTokenProvider;
import com.yogosaza.oms2.user.dto.UserLoginRequestDto;
import com.yogosaza.oms2.user.dto.UserLoginResponseDto;
import com.yogosaza.oms2.user.dto.UserRequestDto;
import com.yogosaza.oms2.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @PostMapping("/create")
    public ResponseEntity<?> create (@RequestBody UserRequestDto dto) throws CommonException {
        userService.create(dto);
        return ResponseEntity.ok("ok");
    }

    @GetMapping("/find")
    public ResponseEntity<?> findUserByLoginId(@RequestParam String loginId) throws CommonException {
        UserResponseDto dto = userService.findByLoginId(loginId);
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UserLoginRequestDto dto) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(dto.getLoginId(), dto.getPassword())
        );

        // 사용자 이름 및 권한 정보 추출
        String authenticatedUsername = authentication.getName();
        List<String> roles = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .toList();


        // JWT 토큰 생성
        String token = jwtTokenProvider.createToken(authenticatedUsername, roles);

        // 응답 생성
        UserLoginResponseDto result = UserLoginResponseDto.builder()
                .loginId(authenticatedUsername)
                .token(token)
                .build();

        return ResponseEntity.ok(result);
    }

}
