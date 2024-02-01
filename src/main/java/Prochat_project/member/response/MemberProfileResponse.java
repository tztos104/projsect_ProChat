package Prochat_project.member.response;

import Prochat_project.member.Members;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberProfileResponse {
    private String memberName;
    private String memberPhone;
    private String memberAddress;
    private String memberAddressDetail;
    private String memberProfile;
    private String memberImage;

    public static MemberProfileResponse fromMember(Members member) {

        return new MemberProfileResponse(
                member.getMemberName(),
                member.getMemberPhone(),
                member.getMemberAddress(),
                member.getMemberAddressDetail(),
                member.getMemberProfile(),
                member.getMemberImage()
        );
    }

}
