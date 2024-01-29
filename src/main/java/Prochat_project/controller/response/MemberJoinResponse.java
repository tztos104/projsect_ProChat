package Prochat_project.controller.response;

import Prochat_project.model.Members;
import Prochat_project.model.MemberRole;
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
