package Prochat_project.model.entity;

import Prochat_project.model.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.hibernate.proxy.HibernateProxy;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@Table(name = "\"members\"")
@SQLDelete(sql = "UPDATE \"members\" SET remove_date = NOW() WHERE id=?")
@Where(clause = "remove_date is NULL")
@NoArgsConstructor
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String memberId;
    private String memberPw;
    private String memberName;
    private String memberEmail;
    private String memberPhone;
    private String memberAddress;
    private String memberAddressDetail;
    private int memberZipcode;
    private String memberProfile;
    private String memberImage;


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


}
