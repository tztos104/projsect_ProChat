package Prochat_project.repository;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class RedisRepository {

    private final RedisTemplate<String, String> redisTemplate;
    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";

    public RedisRepository(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    // ... 기존 필드 및 생성자

    public boolean isTokenBlacklisted(String token) {
        String blacklistKey = BLACKLIST_KEY_PREFIX + token;
        return redisTemplate.hasKey(blacklistKey);
    }

    public void blacklistToken(String token) {
        String blacklistKey = BLACKLIST_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(blacklistKey, "블랙리스트 등록");
    }
}
