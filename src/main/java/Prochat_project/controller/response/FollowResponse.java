package Prochat_project.controller.response;

import Prochat_project.model.Follow;
import Prochat_project.member.Members;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class FollowResponse {

    int id;
    private Members following;
    private Members follower;
    private Timestamp follow_date;

    public static FollowResponse fromFollow(Follow follow) {
        return new FollowResponse(
                follow.getId(),
                follow.getFollowing(),
                follow.getFollower(),
                follow.getFollow_date()
        );
    }

}
