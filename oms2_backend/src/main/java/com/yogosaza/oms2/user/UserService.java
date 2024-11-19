package com.yogosaza.oms2.user;


import com.yogosaza.oms2.user.dto.UserRequestDto;
import com.yogosaza.oms2.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public void create(UserRequestDto dto) {
        UserEntity user = UserEntity.builder()
                .loginId(dto.getLoginId())
                .password(dto.getPassword())
                .name(dto.getName())
                .build();

        UserEntity save = userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public UserResponseDto findByLoginId(String loginId) {
        UserEntity user = userRepository.findByLoginId(loginId).get();
        return UserResponseDto.builder()
                .loginId(user.getLoginId())
                .name(user.getName())
                .build();
    }

}
