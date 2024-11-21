package com.yogosaza.oms2.security;

import com.yogosaza.oms2.user.UserEntity;
import com.yogosaza.oms2.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    // username이 식별자 역할을 한다. 커스텀할거면 username 대신 들어갈 값을 넣는다
    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        System.out.println(user.getRoles());

        // roles를 GrantedAuthority로 변환
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        System.out.println("CustomUserDetailsService");
        System.out.println(authorities);

        return new User(user.getLoginId(), user.getPassword(), authorities);
    }
}
