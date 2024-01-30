package Prochat_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberLogoutRequest {
    private String memberId;
    private String token;

    public MemberLogoutRequest() {
    }
}
