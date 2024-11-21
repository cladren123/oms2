package com.yogosaza.oms2.user;


import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.user.dto.UserRequestDto;
import com.yogosaza.oms2.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public void create(UserRequestDto dto) throws CommonException {
        if (userRepository.existsByLoginId(dto.getLoginId())) {
            throw new CommonException("USER_DUPLICATE", "loginId가 이미 사용 중 입니다.");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword()); // 비밀번호 암호화

        UserEntity user = UserEntity.builder()
                .loginId(dto.getLoginId())
                .password(encodedPassword)
                .roles(List.of("ROLE_USER"))
                .name(dto.getName())
                .build();

        UserEntity save = userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByLoginId(String loginId) throws CommonException{
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CommonException("USER_NOT_FOUND", "해당 loginId가 없습니다."));

        return UserResponseDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .build();
    }

}
