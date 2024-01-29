package Prochat_project.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.time.Instant;

@Getter
@Setter
@Entity
@Table(name = "follow")
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    @ManyToOne
    @JoinColumn(name = "following_id")
    private MemberEntity following;

    @ManyToOne
    @JoinColumn(name = "follower_id")
    private MemberEntity follower;

    private Timestamp follow_date;

    @PrePersist
    void follow_date() {
        this.follow_date = Timestamp.from(Instant.now());
    }

    public static FollowEntity of(MemberEntity follower, MemberEntity following) {
        FollowEntity entity = new FollowEntity();
        entity.setFollowing(following);
        entity.setFollower(follower);
        return entity;
    }

}