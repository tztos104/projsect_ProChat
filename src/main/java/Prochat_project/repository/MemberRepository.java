package Prochat_project.repository;


import Prochat_project.model.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<MemberEntity, Integer> {
    Optional<MemberEntity> findByMemberId(String memberId);
    List<MemberEntity> findByMemberNameLike(String memberName);
}