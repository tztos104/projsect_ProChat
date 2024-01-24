package Prochat_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinRequest {

    private String memberId;
    private String memberPw;

    public MemberJoinRequest() {
    }
}
