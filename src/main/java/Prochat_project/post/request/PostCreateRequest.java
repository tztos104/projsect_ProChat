package Prochat_project.post.request;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostCreateRequest {

    private String title;
    private String content;

    public PostCreateRequest() {
    }
}
