package Prochat_project.config.filter;

import Prochat_project.member.Members;
import Prochat_project.repository.CacheRepository;
import Prochat_project.member.MemberService;
import Prochat_project.util.JwtTokenUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {

    private final String secretKey;
    private final MemberService  memberService;
    private final static List<String> TOKEN_IN_PARAM_URLS = List.of("/api/v1/users/alarm/subscribe");
    private final static List<String> BLACK_PARAM_URLS = List.of("/api/v1/member/logout");
    private final static List<String> BLACKALL_PARAM_URLS = List.of("/api/v1/member/logout/all");
    private final CacheRepository cacheRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //header
        final String header = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String token;


        try {
            // URL에 토큰이 포함된 경우
            if (TOKEN_IN_PARAM_URLS.contains(request.getRequestURI())) {
                log.info("Request with {} check the query param", request.getRequestURI());
                token = request.getQueryString().split("=")[1].trim();
            } else if (header == null || !header.startsWith("Bearer ")) {
                log.error("Authorization Header does not start with Bearer {}", request.getRequestURI());
                filterChain.doFilter(request, response);
                return;
            }else {
                // Bearer 헤더에서 토큰 추출
                token = header.split(" ")[1].trim();
            }

            String memberId = JwtTokenUtils.getMemberId(token, secretKey);
            Members userDetails = memberService.loadUserByUserName(memberId);


            if (!JwtTokenUtils.validate(token, userDetails.getUsername(), secretKey)) {
                filterChain.doFilter(request, response);
                return;
            }
            // 사용자 정보로 인증 객체 생성 및 컨텍스트에 설정
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    userDetails, null,
                    userDetails.getAuthorities()
            );
            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            if (BLACK_PARAM_URLS.contains(request.getRequestURI()) && cacheRepository.isBlackListUserOne(token,memberId)) {
                log.error("BlackList User");
                filterChain.doFilter(request, response);
                return;
            }else if(BLACKALL_PARAM_URLS.contains(request.getRequestURI()) && cacheRepository.isBlackListUserAll(token,memberId)){
                log.error("BlackList User:모든기기에서 로그아웃");
                filterChain.doFilter(request, response);
                return;
            }


        } catch (RuntimeException e) {

            filterChain.doFilter(request, response);
            return;
        }

        filterChain.doFilter(request, response);
    }

}


