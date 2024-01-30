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

import java.util.Date;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    //비밀번호 인코딩하여 저장
    private final BCryptPasswordEncoder encoder;
    private final AlarmRepository alarmRepository;
    private final CacheRepository redisRepository;
    private final RedisTemplate<String, String> redisTemplate;

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;


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
        String token = JwtTokenUtils.generateAccessToken(memberId, secretKey, expiredTimeMs);

        return  token;
    }
    //로그아웃 로직
    @Transactional
    public void logout(String token, String memberId){
        Members savedUser = loadUserByUserName(memberId);
        if (redisRepository.isBlackListUserOne(token, memberId)) {

            throw new RuntimeException("Logout denied for blacklisted user");
        }
        redisRepository.setBlackListUser(token,savedUser);
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



    //프로필 업데이트
    @Transactional
    public Members updateProfile(String memberId, String memberName, String memberPhone, String memberAddress,
                                 String memberProfile, String memberImage) {
        MemberEntity entity = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProchatException(ErrorCode.USER_NOT_FOUND, "멤버를 찾을 수 없습니다"));


        entity.setMemberName(memberName);
        entity.setMemberPhone(memberPhone);
        entity.setMemberAddress(memberAddress);
        entity.setMemberProfile(memberProfile);
        entity.setMemberImage(memberImage);

          // 업데이트된 멤버 정보로 변환하여 반환
        return Members.fromEntity(memberRepository.saveAndFlush(entity));
    }

    //비밀번호 업데이트

    @Transactional
    public Members updatePassword(String memberId, String memberPw, String newPassword) {
        MemberEntity member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProchatException(ErrorCode.USER_NOT_FOUND, "멤버를 찾을 수 없습니다"));

        // 현재 비밀번호 확인
        if (!encoder.matches(memberPw, member.getMemberPw())) {
            System.out.println(memberPw+":"+member.getMemberPw());
            throw new ProchatException(ErrorCode.INVALID_PASSWORD, "현재 비밀번호가 일치하지 않습니다");
        }
        // 새로운 비밀번호로 업데이트
        member.setMemberPw(encoder.encode(newPassword));
        // 업데이트된 멤버 정보로 변환하여 반환
        return Members.fromEntity(memberRepository.save(member));
    }







    @Transactional
    public Page<Alarm> alarmList(Long MembersId, Pageable pageable) {
        return alarmRepository.findAllByMembersId(MembersId, pageable).map(Alarm::fromEntity);
    }


}
