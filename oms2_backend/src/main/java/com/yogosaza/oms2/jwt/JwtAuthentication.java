package com.yogosaza.oms2.jwt;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

// 인증된 사용자 정보 표시
// spring security는 jwtAuthentication을 통해 사용자 인증, 역할 확인
public class JwtAuthentication extends AbstractAuthenticationToken {

    private final String username;
    private final List<String> roles;

    public JwtAuthentication(String username, List<String> roles) {
        super(roles.stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList()));
        this.username = username;
        this.roles = roles;
        setAuthenticated(true); // JWT가 유효하면 인증된 상태로 설정
    }

    @Override
    public Object getCredentials() {
        return null; // JWT 인증에는 비밀번호가 필요하지 않음
    }

    @Override
    public Object getPrincipal() {
        return username;
    }

    public List<String> getRoles() {
        return roles;
    }

}
