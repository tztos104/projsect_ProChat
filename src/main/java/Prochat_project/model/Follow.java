package Prochat_project.model;

import Prochat_project.member.Members;
import Prochat_project.model.entity.FollowEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Follow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;
    private Members following;
    private Members follower;
    private Timestamp follow_date;





    public static Follow fromEntity(FollowEntity entity) {
        return new Follow(
                entity.getId(),
                Members.fromEntity(entity.getFollower()),
                Members.fromEntity(entity.getFollowing()),
                entity.getFollow_date()

        );
    }
}
