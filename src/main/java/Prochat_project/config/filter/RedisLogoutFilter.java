package Prochat_project.config.filter;

import Prochat_project.model.Members;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@RequiredArgsConstructor
public class RedisLogoutFilter extends OncePerRequestFilter {
    private final RedisTemplate<String, String> redisTemplate;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        if (isLogoutRequest(request)) {
            // 로그아웃 요청이라면 처리
            handleLogout();
        }

        // 다음 필터로 계속 진행
        filterChain.doFilter(request, response);
    }

    private boolean isLogoutRequest(HttpServletRequest request) {
        // 로그아웃을 판단하는 로직 추가 (예: 특정 URL 패턴 등)
        // 여기에서는 "/logout"로 시작하는 URL이 로그아웃 요청으로 간주하였습니다.
        return request.getRequestURI().startsWith("/api/v1/member/logout");
    }

    private void handleLogout() {
        // 로그아웃 시 처리할 로직 추가
        Members member = (Members) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (redisTemplate.opsForValue().get("JWT_TOKEN:" + member.getMemberId()) != null) {
            redisTemplate.delete("JWT_TOKEN:" + member.getMemberId());
        }
    }

}

