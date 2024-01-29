package Prochat_project.service;

import Prochat_project.exception.ErrorCode;
import Prochat_project.exception.ProchatException;
import Prochat_project.model.Members;
import Prochat_project.model.alarm.Alarm;
import Prochat_project.model.entity.MemberEntity;
import Prochat_project.repository.AlarmRepository;
import Prochat_project.repository.CacheRepository;
import Prochat_project.repository.MemberRepository;
import Prochat_project.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    //비밀번호 인코딩하여 저장
    private final BCryptPasswordEncoder encoder;
    private final AlarmRepository alarmRepository;
    private final CacheRepository redisRepository;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    private static final String BLACKLIST_KEY_PREFIX = "jwt:blacklist:";



    public Members loadUserByUserName(String memberId) throws UsernameNotFoundException {
        return redisRepository.getUser(memberId).orElseGet(
                () -> memberRepository.findByMemberId(memberId).map(Members::fromEntity).orElseThrow(
                        () -> new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", memberId))
                ));
    }
    //로그인
    public String login(String memberId, String memberPw) {
        Members savedUser = loadUserByUserName(memberId);
        redisRepository.setUser(savedUser);
        //비밀번호 체크
        if (!encoder.matches(memberPw, savedUser.getPassword())) {
            throw new ProchatException(ErrorCode.INVALID_PASSWORD);
        }
        return JwtTokenUtils.generateAccessToken(memberId, secretKey, expiredTimeMs);
    }


    //회원가입 로직
    @Transactional
    public Members join(String memberId, String memberPw, String memberEmail){

        memberRepository.findByMemberId(memberId).ifPresent(it ->{
            throw new ProchatException(ErrorCode.DUPLICATED_MEMBER_ID, String.format("%s 는 이미 있는 아이디입니다",memberId));
        });
        MemberEntity savedUser = memberRepository.save(MemberEntity.of(memberId, encoder.encode(memberPw), memberEmail));
        return Members.fromEntity(savedUser);

    }



  /*  //로그인
    public String login(String memberId, String memberPw ){
        //회원가입 여부체크
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND,String.format("%s 는 없는 아이디 입니다.",memberId)));

        //비밀번호 체크
        if(!encoder.matches(memberPw, memberEntity.getMemberPw())){
            throw new ProchatException(ErrorCode.INVALID_PASSWORD, "비밀번호가 틀렸습니다.");

        }
        //토큰생성
        String token = JwtTokenUtils.generateToken(memberId, secretKey, expiredTimeMs);
        String key = BLACKLIST_KEY_PREFIX + token;
        redisTemplate.opsForValue().set("JWT_TOKEN:" + memberId, token);

        return token;

    }*/




    @Transactional
    public Page<Alarm> alarmList(Long MembersId, Pageable pageable) {
        return alarmRepository.findAllByMembersId(MembersId, pageable).map(Alarm::fromEntity);
    }


}
