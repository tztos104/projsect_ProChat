package Prochat_project.controller.response;

import Prochat_project.model.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class PostResponse {

    private Long id;
    private String title;
    private String content;
    private MemberResponse member;
    private Timestamp regDate;
    private Timestamp updateDate;
    private Timestamp removerDate;

    public static PostResponse fromPost(Post post) {
        return new PostResponse(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                MemberResponse.fromUser(post.getMember()),
                post.getRegDate(),
                post.getUpdateDate(),
                post.getRemoveDate()
        );
    }
}
