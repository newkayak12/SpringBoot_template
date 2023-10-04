package com.server.base.components.configure.security;

import com.server.base.components.configure.ConfigMsg;
import com.server.base.components.configure.security.jwt.JwtAccessDenialHandler;
import com.server.base.components.configure.security.jwt.JwtAuthenticationEntryPoint;
import com.server.base.components.configure.security.jwt.JwtSecurityConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import javax.annotation.PostConstruct;


@EnableWebSecurity(debug = true)
@Configuration(value = "security_configuration")
@DependsOn(value = {"JwtAuthenticationEntryPoint", "JwtAccessDeniedHandler", "JwtSecurityConfig"} )
@RequiredArgsConstructor
public class Config {
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final JwtAccessDenialHandler jwtAccessDeniedHandler;
    private final JwtSecurityConfig jwtSecurityConfig;


    /**
     * 인증(Authentication) : 요청한 사람이 본인인지 확인하는 절차
     * 인가(Authorization) : 인증된 사용자가 접근한 자원에 권한이 있는지 확인하는 절차
     *
     *
     *  시큐리티 필터 순서
     *
     * Security filter chain: [
     *   DisableEncodeUrlFilter : 세션 ID가 URL에 포함되는 것에 막기 위해 HttpServletResponse를 사용해서 URL이 인코딩 되는 것을 막기 위한 필터
     *   WebAsyncManagerIntegrationFilter : SpringSecurityContextHolder는 기본적으로 ThreadLocal 기반으로 동작하는데, 비동기와 관련된 기능을 쓸 때에도 SecurityContext를 사용할 수 있도록 만드러주는 필터
     *   SecurityContextPersistenceFilter : SecruityContext가 없으면 만들어주는 필터
     *                                    : SecurityContext는 Authentication 객체를 보관하는 인터페이스
     *                                    : SecurityContext를 통해 한 요청이 어떤 필터에서도 같은 Authentication 객체를 사용할 수 있다.
     *   HeaderWriterFilter : 응답에 Security 관련 헤더 값을 설정해주는 필터
     *   [CsrfFilter] : CSRF 공격을 방어하기 위한 필터 ( jwt 토큰을 사용하면 세션, 쿠기 기반으로 하지 않기 때문에 꺼뒀음 )
     *   CorsFilter : Cross-Origin Resource Sharing 관련 설정
     *   LogoutFilter : 로그아웃 요청을 처리하는 필터
     *   JwtFilter : 커스텀 Jwt 필터
     *   UsernamePasswordAuthenticationFilter : username, password를 쓰는 form 기반 인증을 처리하는 필터, AuthenticatoinManager를 통한 인증을 실행한다.
     *                                        : 성공하면 Authentication 객체를 SecurityHolder에 저장하고 AuthenticationSuccessHandler를 실행
     *   BasicAuthenticationFilter : HTTP header에 인증 값을 담아 보내는 BASIC 인증을 처리하는 필터
     *   RequestCacheAwareFilter : 인증 처리 후 원래의 Request 정보로 재구성하는 필터
     *   SecurityContextHolderAwareRequestFilter : 서블릿 API 보안 메소드를 구현하는 요청 래퍼로 서블릿 요청을 채우는 필터
     *   AnonymousAuthenticationFilter : 익명 사용자와 관련된 처리를 하는 필터( 여기에 도달할 때까지 인증이 패스되이 않았으면 익명 사용자일 가능성이 높다.)
     *   SessionManagementFilter : 세션 생성 전략을 설정하는 필터, 최대 동시 접속 세션을 설정하고, 유효하지 않는 세션으로 접근했을 떄의 처리, 세션 변조 공격 방지 등의 처리를 담당
     *   ExceptionTranslationFilter : 필터 처리 과정에서 인증 예외 또는 인가 예외가 발생하는 경우 에러 처리 필터
     *   AuthorizationFilter
     * ]
     *
     *
     *
     */

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
       return   httpSecurity
                .csrf().disable() //token 사용이므로 csrf disable
                .cors().and() //cors관련 설정

                .exceptionHandling(
                        handler -> {
                            handler.authenticationEntryPoint(jwtAuthenticationEntryPoint); // 자격 증명 관련 에러 핸들링
                            handler.accessDeniedHandler(jwtAccessDeniedHandler); // 관련 권한 에러 핸들링
                        }
                )

                .sessionManagement(config -> config.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests() // /api/user/* 는 모두 허용
                .antMatchers("/api/v1/user/sign/in").permitAll()
                .anyRequest().authenticated() //이외는 허용하지 않음
                .and()

                .apply(jwtSecurityConfig).and()
                .build();
    }


    @PostConstruct
    public void enabled(){
        ConfigMsg.msg("Spring Security");
    }

}
