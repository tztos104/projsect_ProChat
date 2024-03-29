package Prochat_project.member;

import Prochat_project.post.PostEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@Table(name = "\"members\"")
@SQLDelete(sql = "UPDATE members SET remove_date = NOW() WHERE id=?")
@Where(clause = "remove_date is NULL")
@NoArgsConstructor
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberId;
    private String memberPw;
    private String memberEmail;
    private String memberName;
    private String memberPhone;
    private String memberAddress;
    private String memberAddressDetail;
    private String memberProfile;
    private String memberImage;
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name = "members_id")
    private List<PostEntity> posts;

    private Timestamp regDate;
    private Timestamp updateDate;
    private Timestamp removeDate;


    @Enumerated(EnumType.STRING)
    private MemberRole role =MemberRole.MEMBER;

    @PrePersist
    void regDate(){
        this.regDate= Timestamp.from(Instant.now());
    }
    @PreUpdate
    void updateDate(){
        this.updateDate= Timestamp.from(Instant.now());
    }



    public static MemberEntity of(String memberId, String memberPw, String memberEmail) {
        MemberEntity entity = new MemberEntity();
        entity.setMemberId(memberId);
        entity.setMemberPw(memberPw);
        entity.setMemberEmail(memberEmail);
        return entity;
    }

    public static MemberEntity profileof(String memberName, String memberPhone, String memberAddress, String memberProfile, String memberImage) {
        MemberEntity entity = new MemberEntity();
        entity.setMemberName(memberName);
        entity.setMemberPhone(memberPhone);
        entity.setMemberAddress(memberAddress);
        entity.setMemberProfile(memberProfile);
        entity.setMemberImage(memberImage);
        return entity;
    }


}
