package Prochat_project.controller;

import Prochat_project.controller.request.MemberJoinRequest;
import Prochat_project.controller.request.MemberLoginRequest;
import Prochat_project.exception.ErrorCode;
import Prochat_project.exception.ProchatException;
import Prochat_project.model.Members;
import Prochat_project.service.MemberService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class MemberControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MemberService memberService;

    @Test
    public void 회원가입() throws Exception {
        String memberId = "memberId";
        String memberPw = "memberPw";

        //TODO
        when(memberService.join(memberId,memberPw)).thenReturn(mock(Members.class));

        mockMvc.perform(post("/api/v1/member/join")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(memberId, memberPw)))
        ).andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void 회원가입시_중복회원가입시_에러반환() throws Exception {
        String memberId = "memberId";
        String memberPw = "memberPw";


        when(memberService.join(memberId,memberPw)).thenThrow(new ProchatException(ErrorCode.DUPLICATED_MEMBER_ID, String.format("%s 는 이미 있는 아이디입니다",memberId)));

        mockMvc.perform(post("/api/v1/member/join")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberJoinRequest(memberId, memberPw)))
                ).andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    public void 로그인() throws Exception {
        String memberId = "memberId";
        String memberPw = "memberPw";

        //TODO
        when(memberService.login(memberId,memberPw)).thenReturn("test_tcken");

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(memberId, memberPw)))
                ).andDo(print())
                .andExpect(status().isOk());

    }
    @Test
    public void 로그인시_비회원일때() throws Exception {
        String memberId = "memberId";
        String memberPw = "memberPw";


        when(memberService.login(memberId,memberPw)).thenThrow(new ProchatException(ErrorCode.DUPLICATED_MEMBER_ID,""));

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(memberId, memberPw)))
                ).andDo(print())
                .andExpect(status().isNotFound());

    }

    @Test
    public void 로그인시_비밀번호오류() throws Exception {
        String memberId = "memberId";
        String memberPw = "memberPw";

        //TODO
        when(memberService.login(memberId,memberPw)).thenThrow(new ProchatException(ErrorCode.DUPLICATED_MEMBER_ID,""));

        mockMvc.perform(post("/api/v1/member/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsBytes(new MemberLoginRequest(memberId, memberPw)))
                ).andDo(print())
                .andExpect(status().isUnauthorized());

    }


}
