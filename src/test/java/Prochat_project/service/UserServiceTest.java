package Prochat_project.service;

import Prochat_project.exception.ErrorCode;
import Prochat_project.exception.ProchatException;
import Prochat_project.fixture.MemberEntityFixture;
import Prochat_project.model.entity.MemberEntity;
import Prochat_project.repository.MemberRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;

import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    private  MemberService memberService;

    @MockBean
    private MemberRepository memberRepository;

    @MockBean
    private BCryptPasswordEncoder encoder;

    @Test
    void 회원가입성공(){
        String memberId = "memberId";
        String memberPw = "memberPw";

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());
        when(encoder.encode(memberPw)).thenReturn("encodedPw");
        when(memberRepository.save(any())).thenReturn(MemberEntityFixture.get(memberId, memberPw));


        Assertions.assertDoesNotThrow(()->memberService.join(memberId,memberPw));
    }

    @Test
    void 회원가입시_아이디중복(){
        String memberId = "memberId";
        String memberPw = "memberPw";
        MemberEntity fixture = MemberEntityFixture.get(memberId, memberPw);

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(fixture));
        when(encoder.encode(memberPw)).thenReturn("encodedPw");
        when(memberRepository.save(any())).thenReturn(Optional.of(fixture));


        ProchatException e = Assertions.assertThrows(ProchatException.class, () -> memberService.join(memberId, memberPw));
        Assertions.assertEquals(ErrorCode.DUPLICATED_MEMBER_ID,e.getErrorCode());
    }


    @Test
    void 로그인성공(){
        String memberId = "memberId";
        String memberPw = "memberPw";

        MemberEntity fixture = MemberEntityFixture.get(memberId, memberPw);

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(fixture));
        when(encoder.matches(memberPw,fixture.getMemberPw())).thenReturn(true);


        Assertions.assertDoesNotThrow(()->memberService.login(memberId,memberPw));
    }

    @Test
    void 로그인시_실패_유저가없는경우(){
        String memberId = "memberId";
        String memberPw = "memberPw";

        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.empty());



        ProchatException e =  Assertions.assertThrows(ProchatException.class,()->memberService.login(memberId,memberPw));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND,e.getErrorCode());
    }

    @Test
    void 로그인시_실패_비밀번호오류(){
        String memberId = "memberId";
        String memberPw = "memberPw";
        String memberPw2 = "memberPw2";

        MemberEntity fixture = MemberEntityFixture.get(memberId, memberPw);
        when(memberRepository.findByMemberId(memberId)).thenReturn(Optional.of(fixture));



        ProchatException e= Assertions.assertThrows(ProchatException.class,()->memberService.login(memberId,memberPw2));
        Assertions.assertEquals(ErrorCode.INVALID_PASSWORD,e.getErrorCode());
    }

}
