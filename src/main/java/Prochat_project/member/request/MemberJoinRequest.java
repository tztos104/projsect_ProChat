package Prochat_project.member.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequest {

    private String memberId;
    private String memberPw;
    private String memberEmail;


    public MemberJoinRequest() {
    }
}
