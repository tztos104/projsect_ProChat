package Prochat_project.service;

import Prochat_project.exception.ErrorCode;
import Prochat_project.exception.ProchatException;
import Prochat_project.fixture.PostEntityFixture;
import Prochat_project.fixture.TestInfoFixture;
import Prochat_project.model.entity.MemberEntity;
import Prochat_project.model.entity.PostEntity;
import Prochat_project.repository.MemberRepository;
import Prochat_project.repository.PostRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
public class PostServiceTest {

    @Autowired
    private PostService postService;

    @MockBean
    private PostRepository postRepository;

    @MockBean
    private MemberRepository memberRepository;


    @Test
    void 포스트작성성공(){
        String title = "제목";
        String content = "내용";
        String writer = "작성자";

        when(memberRepository.findByMemberId(writer)).thenReturn(Optional.of(mock(MemberEntity.class)));
        when(postRepository.save(any())).thenReturn(mock(PostEntity.class));

        Assertions.assertDoesNotThrow(()->postService.create(title,content,writer));

    }

    @Test
    void 포스트작성실패_비회원(){
        String title = "제목";
        String content = "내용";
        String writer = "작성자";

        when(memberRepository.findByMemberId(writer)).thenReturn(Optional.empty());
        when(postRepository.save(any())).thenReturn(mock(PostEntity.class));

        ProchatException e = Assertions.assertThrows(ProchatException.class, () -> postService.create(title, content, writer));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, e.getErrorCode());
    }

//   @Test
//   void 포스트_수정성공(){
//       String title = "제목";
//       String content = "내용";
//       String writer = "작성자";
//       Long postId = 1L;
//
//       PostEntity postEntity = PostEntityFixture.get(writer, postId);
//       MemberEntity memberEntity = postEntity.getMember();
//
//       when(memberRepository.findByMemberId(writer)).thenReturn(Optional.of(memberEntity));
//       when(postRepository.findById(postId)).thenReturn(Optional.of(postEntity));
//
//       Assertions.assertDoesNotThrow(()-> postService.modify(title,content,writer,postId));
//
//
//
//   }



    @Test
    void 포스트_수정시_포스트가_존재하지_않으면_에러를_내뱉는다() {
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.empty());
        when(memberRepository.findByMemberId(fixture.getUserName())).thenReturn(Optional.empty());
        ProchatException exception = Assertions.assertThrows(ProchatException.class, () ->
                postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getContent()));
        Assertions.assertEquals(ErrorCode.POST_NOT_FOUND, exception.getErrorCode());
    }



    @Test
    void 포스트_수정시_유저가_존재하지_않으면_에러를_내뱉는다() {

        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();

        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mock(PostEntity.class)));
        when(memberRepository.findByMemberId(fixture.getUserName())).thenReturn(Optional.empty());
        ProchatException exception = Assertions.assertThrows(ProchatException.class, () ->
                postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getContent()));
        Assertions.assertEquals(ErrorCode.USER_NOT_FOUND, exception.getErrorCode());
    }


    @Test
    void 포스트_수정시_포스트_작성자와_유저가_일치하지_않으면_에러를_내뱉는다() {
        PostEntity mockPostEntity = mock(PostEntity.class);
        MemberEntity mockUserEntity = mock(MemberEntity.class);
        TestInfoFixture.TestInfo fixture = TestInfoFixture.get();
        when(postRepository.findById(fixture.getPostId())).thenReturn(Optional.of(mockPostEntity));
        when(memberRepository.findByMemberId(fixture.getUserName())).thenReturn(Optional.of(mockUserEntity));
        when(mockPostEntity.getMembers()).thenReturn(mock(MemberEntity.class));
        ProchatException exception = Assertions.assertThrows(ProchatException.class, () ->
                postService.modify(fixture.getUserId(), fixture.getPostId(), fixture.getTitle(), fixture.getContent()));
        Assertions.assertEquals(ErrorCode.INVALID_PERMISSION, exception.getErrorCode());
    }


}
