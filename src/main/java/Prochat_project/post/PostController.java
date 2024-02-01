package Prochat_project.post;


import Prochat_project.post.request.PostCommentRequest;
import Prochat_project.post.request.PostCreateRequest;
import Prochat_project.controller.response.CommentResponse;
import Prochat_project.post.response.PostResponse;
import Prochat_project.controller.response.Response;
import Prochat_project.member.Members;
import Prochat_project.util.ClassUtils;
import lombok.AllArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {

    private  final PostService postService;

    @PostMapping
    public Response<Void> create(@RequestBody PostCreateRequest request, Authentication authentication){

        postService.create(request.getTitle(), request.getContent(), authentication.getName());
        return Response.success();
    }

    @PutMapping("/{postId}")
    public Response<PostResponse> modify(@PathVariable(name = "postId") Long postId, @RequestBody PostCreateRequest request, Authentication authentication){

        Members members = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);

        return Response.success(PostResponse.fromPost(
                postService.modify(members.getId(), postId, request.getTitle(), request.getContent())));
    }

    @DeleteMapping("/{postId}")
    public Response<Void> delete(@PathVariable(name = "postId") Long postId, Authentication authentication) {
        Members members = ClassUtils.getSafeCastInstance(authentication.getPrincipal(), Members.class);
        postService.delete(members.getId(), postId);
        return Response.success();
    }

    @GetMapping("/{postId}/comments")
    public Response<Page<CommentResponse>> getComments(Pageable pageable, @PathVariable(name = "postId") Long postId) {
        return Response.success(postService.getComments(postId, pageable).map(CommentResponse::fromComment));
    }
    @PostMapping("/{postId}/comments")
    public Response<Void> comment(@PathVariable(name = "postId") Long postId, @RequestBody PostCommentRequest request, Authentication authentication) {
        postService.comment(postId, authentication.getName(), request.getComment());
        return Response.success();
    }

    @GetMapping("/{postId}/likes")
    public Response<Integer> getLikes(@PathVariable(name = "postId") Long postId, Authentication authentication) {
        return Response.success(postService.getLikeCount(postId));
    }

    @PostMapping("/{postId}/likes")
    public Response<Void> like(@PathVariable(name = "postId") Long postId, Authentication authentication) {
        postService.postLike(postId, authentication.getName());
        return Response.success();
    }

    @PostMapping("/comments/likes")
    public Response<Void> commentLike(@RequestParam(name = "commentId") Long commentId,Authentication authentication) {
        postService.commentLike(commentId, authentication.getName());
        return Response.success();
    }

}
