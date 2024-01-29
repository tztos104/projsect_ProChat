package Prochat_project.repository;

import Prochat_project.model.Members;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;
import java.time.Duration;
import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class CacheRepository {
    private final RedisTemplate<String, Members> RedisTemplate;

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);


    public void setUser(Members member) {
        String key = getKey(member.getUsername());
        log.info("Set User to Redis {}({})", key, member);
        RedisTemplate.opsForValue().set(key, member, USER_CACHE_TTL);
    }



    public Optional<Members> getUser(String userName) {
        Members data = RedisTemplate.opsForValue().get(getKey(userName));
        log.info("Get User from Redis {}", data);
        return Optional.ofNullable(data);
    }

     private String getKey(String userName) {
        return "UID:" + userName;
    }



    // 블랙리스트에 있는지 확인
    public boolean isBlacklisted(String token) {
        String key = getBlacklistKey(token);
        return RedisTemplate.hasKey(key);
    }

    private String getBlacklistKey(String token) {
        return "BLACKLIST:" + token;
    }



}
