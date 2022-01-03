package com.ex0dus1.springboot.config.auth;

import com.ex0dus1.springboot.domain.user.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@RequiredArgsConstructor
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private final CustomOAuth2UserService customOAuth2UserService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().headers().frameOptions().disable()
                .and()
                    .authorizeRequests()
                    // antMatchers: 권한 관리 대상 지정. url별, 메소드별 관리 가능
                    .antMatchers("/", "/css/**", "/images/**","/js/**", "/h2-console/**").permitAll()
                    .antMatchers("/api/v1/**").hasRole(Role.USER.name())
                    .anyRequest().authenticated() // 나머지 url들을 인증된 사용자(로그인한 사용자)에게만 허용
                .and() // 로그아웃 기능
                    .logout().logoutSuccessUrl("/")
                .and() // 로그인 성공 시 후속 조치
                    .oauth2Login()
                    .userInfoEndpoint()
                    .userService(customOAuth2UserService);
    }
}
