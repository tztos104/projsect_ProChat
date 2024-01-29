package Prochat_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PasswordRequest {

    private String memberPw;
    private String newPassword;

    public PasswordRequest() {
    }
}
