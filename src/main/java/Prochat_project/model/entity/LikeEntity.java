package Prochat_project.model.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.sql.Timestamp;
import java.time.Instant;

@Setter
@Getter
@Entity
@Table(name = "\"like\"")
@SQLDelete(sql = "UPDATE like SET remove_date = NOW() WHERE id=?")
@Where(clause = "remove_date is NULL")
@NoArgsConstructor
public class LikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id = null;

    @ManyToOne
    @JoinColumn(name = "members_id")
    private MemberEntity member;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private CommentEntity comment;

    @Column(name = "reg_date")
    private Timestamp regDate;

    @Column(name = "update_date")
    private Timestamp updateDate;

    @Column(name = "remove_date")
    private Timestamp removeDate;

    @PrePersist
    void regDate() {
        this.regDate = Timestamp.from(Instant.now());
    }

    @PreUpdate
    void updateDate() {
        this.updateDate = Timestamp.from(Instant.now());
    }

    public static LikeEntity ofPost(PostEntity post, MemberEntity member) {
        LikeEntity entity = new LikeEntity();
        entity.setPost(post);
        entity.setMember(member);
        return entity;
    }
    public static LikeEntity ofComment(CommentEntity comment, MemberEntity member) {
        LikeEntity entity = new LikeEntity();
        entity.setComment(comment);;
        entity.setMember(member);
        return entity;
    }
}
