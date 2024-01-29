package Prochat_project.model;

import Prochat_project.model.entity.LikeEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;


@Getter
@AllArgsConstructor
public class Like {
    private Long id;
    private Long memberId;
    private String memberName;
    private Long postId;
    private Timestamp registeredAt;
    private Timestamp updatedAt;
    private Timestamp removedAt;


    public static Like fromEntity(LikeEntity entity) {
        return new Like(
                entity.getId(),
                entity.getMember().getId(),
                entity.getMember().getMemberId(),
                entity.getPost().getId(),
                entity.getRegDate(),
                entity.getUpdateDate(),
                entity.getRemoveDate()
        );
    }
}
