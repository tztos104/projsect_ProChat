package Prochat_project.controller.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileRequest {
    private String memberName;
    private String memberPhone;
    private String memberAddress;
    private String memberAddressDetail;
    private String memberProfile;
    private String memberImage;

    public MemberProfileRequest() {
    }
}
