package Prochat_project.service;

import Prochat_project.exception.ErrorCode;
import Prochat_project.exception.ProchatException;
import Prochat_project.model.Members;
import Prochat_project.model.entity.MemberEntity;
import Prochat_project.repository.MemberRepository;
import Prochat_project.util.JwtTokenUtils;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    //비밀번호 인코딩하여 저장
    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret-key}")
    private String secretKey;
    @Value("${jwt.token.expired-time-ms}")
    private Long expiredTimeMs;

    public Members join(String memberId, String memberPw){


        memberRepository.findByMemberId(memberId).ifPresent(it ->{
            throw new ProchatException(ErrorCode.DUPLICATED_MEMBER_ID, String.format("%s 는 이미 있는 아이디입니다",memberId));
        });
        MemberEntity memberEntity = memberRepository.save(MemberEntity.of(memberId, encoder.encode(memberPw)));


        return Members.fromEntity(memberEntity);

    }

    public String login(String memberId, String memberPw ){
        //회원가입 여부체크
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() -> new ProchatException(ErrorCode.USER_NOT_FOUND,String.format("%s 는 없는 아이디 입니다.",memberId)));

        //비밀번호 체크
        if(!encoder.matches(memberPw, memberEntity.getMemberPw())){
            throw new ProchatException(ErrorCode.INVALID_PASSWORD);

        }
        //토큰생성
        String token = JwtTokenUtils.generateToken(memberId, secretKey, expiredTimeMs);

        return token;

    }

}
