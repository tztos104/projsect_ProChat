package Prochat_project.member;


import Prochat_project.member.request.*;
import Prochat_project.member.response.MemberJoinResponse;
import Prochat_project.member.response.MemberLoginResponse;
import Prochat_project.member.response.MemberProfileResponse;
import Prochat_project.model.entity.FollowEntity;
import Prochat_project.controller.response.AlarmResponse;
import Prochat_project.controller.response.Response;
import Prochat_project.service.AlarmService;
import Prochat_project.service.FollowService;
import Prochat_project.util.ClassUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final FollowService followService;
    private final AlarmService alarmService;


    @PostMapping("/join")
    public Response<MemberJoinResponse> join(@RequestBody MemberJoinRequest request) {
        return Response.success(MemberJoinResponse.fromMember(
                memberService.join(request.getMemberId(), request.getMemberPw(), request.getMemberEmail())));
    }


    @PostMapping("/login")
    public Response<MemberLoginResponse> login(@RequestBody MemberLoginRequest request) {
        String token = memberService.login(request.getMemberId(), request.getMemberPw());

        return Response.success(new MemberLoginResponse(token));

    }

    @PostMapping("/logout")
    public Response<MemberLoginResponse> logout(@RequestBody MemberLogoutRequest request, Authentication authentication) {
        Members members = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);

        memberService.logout(request.getToken(), members.getMemberId());

        return Response.success(new MemberLoginResponse(request.getToken()));
    }
    @PostMapping("/logout/all")
    public Response<MemberLoginResponse> logoutAll(@RequestBody MemberLogoutRequest request,Authentication authentication) {
        Members members = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);

        memberService.logout(request.getToken(), members.getMemberId());

        return Response.success(new MemberLoginResponse(request.getToken()));
    }

    @PutMapping("/profile_update")
    public Response<MemberProfileResponse> updateProfile(@RequestBody MemberProfileRequest request, Authentication authentication) {
        Members members = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);

        return Response.success(MemberProfileResponse.fromMember(
                memberService.updateProfile(
                        members.getMemberId(), request.getMemberName(), request.getMemberPhone(),
                        request.getMemberAddress(), request.getMemberProfile(), request.getMemberImage())));


    }


    //비밀번호업데이트
    @PutMapping("/password_update")
    public Response<Void> updatePassword(@RequestBody PasswordRequest request, Authentication authentication) {
        Members members = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);

        memberService.updatePassword(members.getMemberId(), request.getMemberPw(), request.getNewPassword());
        memberService.logout(request.getToken(), members.getMemberId());
        return Response.success();
    }



    @GetMapping("/me")
    public Response<MemberJoinResponse> me(Authentication authentication) {

        return Response.success(MemberJoinResponse.fromMember(memberService.loadUserByUserName(authentication.getName())));
    }

    @PutMapping("/follow")
    public Response<Void> follow(@RequestParam(name ="memberId") String memberId, Authentication authentication) {
        followService.follow(memberId, authentication.getName());
        return Response.success();
    }

    @DeleteMapping("/unfollow")
    public Response<Void> unfollow(@RequestParam(name ="memberId") String memberId, Authentication authentication) {
        followService.unfollow(memberId, authentication.getName());
        return Response.success();
    }

    @GetMapping("/followers/{memberId}")
    public Response<List<FollowEntity>> getFollowers(@PathVariable(name = "memberId") String memberId) {
        List<FollowEntity> followers = followService.getFollowers(memberId);
        return Response.success(followers);
    }

    @GetMapping("/following/{memberId}")
    public ResponseEntity<List<FollowEntity>> getFollowing(@PathVariable(name = "memberId") String memberId) {
        List<FollowEntity> following = followService.getFollowing(memberId);
        return ResponseEntity.ok(following);
    }


    @GetMapping("/alarm")
    public Response<Page<AlarmResponse>> alarm(Pageable pageable, Authentication authentication) {
        Members member = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);
        return Response.success(memberService.alarmList(member.getId(), pageable).map(AlarmResponse::fromAlarm));
    }

    @GetMapping(value = "/alarm/subscribe")
    public SseEmitter subscribe(Authentication authentication) {
        log.info("subscribe");
        Members user = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);
        return alarmService.connectNotification(user.getId());
    }





}
