package Prochat_project.member.response;

import Prochat_project.member.Members;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberResponse {
    private Long id;
    private String memberId;

    public static MemberResponse fromUser(Members member) {
        return new MemberResponse(
                member.getId(),
                member.getMemberId()
        );
    }
}
