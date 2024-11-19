package com.yogosaza.oms2.security;

import com.yogosaza.oms2.user.UserEntity;
import com.yogosaza.oms2.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username이 식별자 역할을 한다. 커스텀할거면 username 대신 들어갈 값을 넣는다
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {

        System.out.println("work1");

        UserEntity user = userRepository.findByLoginId(loginId).orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println(user.getLoginId());
        System.out.println(user.getPassword());

        System.out.println("work2");

        return new User(user.getLoginId(), user.getPassword(), new ArrayList<>());
    }
}
