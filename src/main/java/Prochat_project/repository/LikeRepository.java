package Prochat_project.repository;

import Prochat_project.model.entity.LikeEntity;
import Prochat_project.model.entity.MemberEntity;
import Prochat_project.model.entity.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
    Optional<LikeEntity> findByMemberAndPost(MemberEntity member, PostEntity post);


    @Query(value = "SELECT COUNT(*) from LikeEntity entity WHERE entity.post = :post")
    Integer countByPost(@Param("post") PostEntity post);

    List<LikeEntity> findAllByPost(PostEntity post);


    @Transactional
    @Modifying
    @Query("UPDATE LikeEntity entity SET entity.removeDate = NOW() where entity.post = :post")
    void deleteAllByPost(@Param("post") PostEntity post);
}
