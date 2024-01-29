package Prochat_project.service;

import Prochat_project.exception.ErrorCode;
import Prochat_project.exception.ProchatException;
import Prochat_project.model.Comment;
import Prochat_project.model.Post;
import Prochat_project.model.entity.CommentEntity;
import Prochat_project.model.entity.LikeEntity;
import Prochat_project.model.entity.MemberEntity;
import Prochat_project.model.entity.PostEntity;
import Prochat_project.repository.CommentRepository;
import Prochat_project.repository.LikeRepository;
import Prochat_project.repository.MemberRepository;
import Prochat_project.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final LikeRepository  likeRepository;

    @Transactional
    public void create(String title, String content, String memberId){

        //유저 찾기
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 찾을 수 없습니다.", memberId)));
        PostEntity postEntity = PostEntity.of(title, content, memberEntity);
        //글저장
        postRepository.save(postEntity);


    }

    // 뉴스피드 보여주기
    public Page<Post> list(Pageable pageable) {
        return postRepository.findAll(pageable).map(Post::fromEntity);
    }

    public Page<Post> my(Long MembersId, Pageable pageable) {
        return postRepository.findAllByMembersId(MembersId, pageable).map(Post::fromEntity);
    }


    @Transactional
    public Post modify(Long Id, Long postId, String title, String content) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() ->
                new ProchatException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));


        if (!Objects.equals(postEntity.getMembers().getId(), Id)) {
            throw new ProchatException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", Id, postId));
        }

        postEntity.setTitle(title);
        postEntity.setContent(content);

        return Post.fromEntity(postRepository.saveAndFlush(postEntity));
    }

    @Transactional
    public void delete(Long userId, Long postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new ProchatException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        if (!Objects.equals(postEntity.getMembers().getId(), userId)) {
            throw new ProchatException(ErrorCode.INVALID_PERMISSION, String.format("user %s has no permission with post %d", userId, postId));
        }
        likeRepository.deleteAllByPost(postEntity);
        commentRepository.deleteAllByPost(postEntity);
        postRepository.delete(postEntity);
    }

    @Transactional
    public void comment(Long postId, String memberId, String comment) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new ProchatException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", memberId)));

        commentRepository.save(CommentEntity.of(comment, postEntity, memberEntity));
    }

    public Page<Comment> getComments(Long postId, Pageable pageable) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new ProchatException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        return commentRepository.findAllByPost(postEntity, pageable).map(Comment::fromEntity);
    }

    @Transactional
    public void like(Long postId, String MemberId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new ProchatException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        MemberEntity memberEntity = memberRepository.findByMemberId(MemberId)
                .orElseThrow(() -> new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("userName is %s", MemberId)));

        likeRepository.findByMemberAndPost(memberEntity, postEntity).ifPresent(it -> {
            throw new ProchatException(ErrorCode.ALREADY_LIKED_POST, String.format("userName %s already like the post %s", MemberId, postId));
        });

        likeRepository.save(LikeEntity.of(postEntity, memberEntity));


    }

    public Integer getLikeCount(Long postId) {
        PostEntity postEntity = postRepository.findById(postId).orElseThrow(() -> new ProchatException(ErrorCode.POST_NOT_FOUND, String.format("postId is %d", postId)));
        List<LikeEntity> likes = likeRepository.findAllByPost(postEntity);
        return likes.size();
    }

}