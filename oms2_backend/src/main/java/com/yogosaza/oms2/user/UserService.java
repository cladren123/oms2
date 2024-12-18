package com.yogosaza.oms2.user;


import com.yogosaza.oms2.exception.CommonException;
import com.yogosaza.oms2.logging.LogInputOutput;
import com.yogosaza.oms2.user.dto.UserRequestDto;
import com.yogosaza.oms2.user.dto.UserResponseDto;
import com.yogosaza.oms2.user.dto.UserUpdateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.TimeUnit;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RedisTemplate<String, UserResponseDto> redisTemplate;

    private final String CACHE_PREFIX = "USER_";
    private final long timeout = 10;

    @LogInputOutput
    public void create(UserRequestDto dto) {
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

    @LogInputOutput
    @Transactional(readOnly = true)
    public UserResponseDto findByLoginId(String loginId) {

        String cacheKey = CACHE_PREFIX + loginId;
        UserResponseDto redisDto = redisTemplate.opsForValue().get(cacheKey);

        if (redisDto != null) {
            System.out.println("Redis 데이터 조회 ");
            return redisDto;
        }


        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CommonException("USER_NOT_FOUND", "해당 loginId가 없습니다."));

        UserResponseDto dbDto = UserResponseDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .build();

        redisTemplate.opsForValue().set(cacheKey, dbDto, timeout, TimeUnit.MINUTES);

        return dbDto;
    }

    @LogInputOutput
    public void update(UserUpdateDto dto) {
        UserEntity user = userRepository.findById(dto.getId())
                .orElseThrow(() -> new CommonException("USER_NOT_FOUND", "해당 id가 없습니다."));

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        String cacheKey = CACHE_PREFIX + user.getLoginId();
        UserResponseDto responseDto = UserResponseDto.builder().loginId(user.getLoginId()).name(user.getName()).build();
        redisTemplate.opsForValue().set(cacheKey, responseDto, timeout, TimeUnit.MINUTES);
    }

}
