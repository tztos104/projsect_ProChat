package Prochat_project.controller;


import Prochat_project.controller.request.MemberJoinRequest;
import Prochat_project.controller.request.MemberLoginRequest;
import Prochat_project.controller.response.MemberJoinResponse;
import Prochat_project.controller.response.MemberLoginResponse;
import Prochat_project.controller.response.Response;
import Prochat_project.model.Members;
import Prochat_project.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @PostMapping("/join")
    public Response<MemberJoinResponse> join(@RequestBody MemberJoinRequest request) {
        Members member = memberService.join(request.getMemberId(), request.getMemberPw());
        return Response.success(MemberJoinResponse.fromMember(member));
    }


    @PostMapping("/login")
    public Response<MemberLoginResponse> login(@RequestBody MemberLoginRequest request) {
        String token = memberService.login(request.getMemberId(), request.getMemberPw());
        return Response.success(new MemberLoginResponse(token));

    }

}
