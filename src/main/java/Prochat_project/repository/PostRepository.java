package Prochat_project.repository;

import Prochat_project.model.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


public interface PostRepository extends JpaRepository<PostEntity, Long> {
    public Page<PostEntity> findAllByMembersId(Long membersID, Pageable pageable);
}