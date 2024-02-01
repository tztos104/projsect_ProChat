package Prochat_project.repository;

import Prochat_project.post.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


public interface PostRepository extends JpaRepository<PostEntity, Long> {
    public Page<PostEntity> findAllByMembersId(Long membersID, Pageable pageable);
}