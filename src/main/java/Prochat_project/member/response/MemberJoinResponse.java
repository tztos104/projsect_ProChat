package Prochat_project.member.response;

import Prochat_project.member.Members;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class MemberJoinResponse {

    private Long id;
    private String memberId;


    public static MemberJoinResponse fromMember(Members member) {
        return new MemberJoinResponse(
                member.getId(),
                member.getMemberId()

        );

    }

}
