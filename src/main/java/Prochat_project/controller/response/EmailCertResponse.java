package Prochat_project.controller.response;

import Prochat_project.model.Follow;
import Prochat_project.model.Members;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class EmailCertResponse {
    private String email;
    private String certificationNumber;

    public static EmailCertResponse fromEmail(String email,String certificationNumber) {
        return new EmailCertResponse(
                email,
                certificationNumber
        );
    }
}
