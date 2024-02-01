package Prochat_project.repository;

import Prochat_project.member.Members;
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
    private final RedisTemplate<String, String> blacklistRedisTemplate;

    private final static Duration USER_CACHE_TTL = Duration.ofDays(3);
    private final static Duration BLACK_USER_CACHE_TTL = Duration.ofMinutes(10);
    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";

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


    public void setBlackListUser(String token, Members member) {
        String blacklistKey = getBlacklistKey(token,member.getUsername());
        log.info("Blacklisting User: {}({})", blacklistKey, member);
        blacklistRedisTemplate.opsForValue().set(blacklistKey, token, BLACK_USER_CACHE_TTL);
    }

    public Optional<Members> getBlackListUser(String userName) {
        Members data = RedisTemplate.opsForValue().get(getKey(userName));
        log.info("Get Blacklisting User from Redis {}", data);
        return Optional.ofNullable(data);
    }

    public Boolean isBlackListUserOne(String token, String userName) {
        String blacklistKey = getBlacklistKey(token,userName);
        System.out.println(blacklistRedisTemplate.opsForValue().get(blacklistKey).matches(token));
        return blacklistRedisTemplate.opsForValue().get(blacklistKey).matches(token);
    }


    public Boolean isBlackListUserAll(String token,String userName) {
        System.out.println("여기오나");
        String blacklistKey = getBlacklistKey(token,userName);
        return blacklistRedisTemplate.hasKey(blacklistKey);
    }
    private String getBlacklistKey( String token,String userName) {
        return BLACKLIST_KEY_PREFIX + userName +token;
    }
}
