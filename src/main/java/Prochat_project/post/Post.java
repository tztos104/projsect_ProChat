package Prochat_project.post;

import Prochat_project.member.Members;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Post {
    private Long id = null;
    private String title;
    private String content;
    private Members member;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Timestamp removeDate;

    public static Post fromEntity(PostEntity entity) {
        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                Members.fromEntity(entity.getMembers()),
                entity.getRegDate(),
                entity.getUpdateDate(),
                entity.getRemoveDate()
        );
    }
}
