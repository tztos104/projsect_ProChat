package Prochat_project.repository;

import Prochat_project.model.entity.FollowEntity;
import Prochat_project.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface FollowRepository extends JpaRepository<FollowEntity, Long> {
    List<FollowEntity> findAllByFollower(MemberEntity follower);
    List<FollowEntity> findAllByFollowing(MemberEntity following);
    Optional<FollowEntity> findByFollowerAndFollowing(MemberEntity followerId, MemberEntity followingId);

    @Query(value = "SELECT COUNT(*) from FollowEntity entity WHERE entity.follower = :memberId")
    Integer countByfollower(@Param("memberId") MemberEntity memberId);

    @Query(value = "SELECT COUNT(*) from FollowEntity entity WHERE entity.following = :memberId")
    Integer countByfollowing(@Param("memberId") MemberEntity memberId);
}
