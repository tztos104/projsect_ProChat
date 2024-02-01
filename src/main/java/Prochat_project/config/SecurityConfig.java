package Prochat_project.config;

import Prochat_project.config.filter.JwtTokenFilter;
import Prochat_project.exception.CustomAuthenticationEntryPoint;
import Prochat_project.repository.CacheRepository;
import Prochat_project.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService memberService;
    @Value("${jwt.secret-key}")
    private  String key;
    private CacheRepository cacheRepository;





    @Bean
    public SecurityFilterChain applicationSecurity(HttpSecurity http) throws Exception {
        http    .cors(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagementConfigurer
                        -> sessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS));


        http.authorizeHttpRequests((authorizeHttpRequests) ->
                authorizeHttpRequests
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                        .requestMatchers("/api/v1/member/join").permitAll()
                        .requestMatchers("/api/v1/member/login").permitAll()
                        .requestMatchers("/api/v1/mails/mail_send").permitAll()
                        .requestMatchers("/api/v1/mails/verify").permitAll()
                        .requestMatchers("/api/v1/member/logout").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/posts").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/posts/{postId}").permitAll()
                        .requestMatchers(HttpMethod.GET,"/api/v1/posts/{postId}/comments").permitAll()
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll()
                        .anyRequest().authenticated()
        );

        http
                .addFilterBefore(new JwtTokenFilter(key, memberService,cacheRepository), UsernamePasswordAuthenticationFilter.class)

                .exceptionHandling(exceptionHandling ->
                        exceptionHandling.authenticationEntryPoint(new CustomAuthenticationEntryPoint()));



        return http.build();
    }
}
