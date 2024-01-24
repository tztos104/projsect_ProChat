package Prochat_project.model.entity;

import Prochat_project.model.MemberRole;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.proxy.HibernateProxy;


import java.sql.Timestamp;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Table
@Entity
public class MemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
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
    private Timestamp deleteDate;


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
    @PreRemove
    void deleteDate(){
        this.deleteDate= Timestamp.from(Instant.now());
    }



    public static MemberEntity of(String memberId, String memberPw) {
        MemberEntity entity = new MemberEntity();
        entity.setMemberId(memberId);
        entity.setMemberPw(memberPw);
        return entity;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        MemberEntity that = (MemberEntity) o;
        return getId() != null && Objects.equals(getId(), that.getId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
