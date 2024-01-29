package Prochat_project.fixture;

import Prochat_project.model.entity.MemberEntity;
import Prochat_project.model.entity.PostEntity;

public class PostEntityFixture {

    public static PostEntity get(String userName, Long postId){
        MemberEntity member = new MemberEntity();
        member.setId(1L);
        member.setMemberId(userName);
        PostEntity post = new PostEntity();
        post.setMembers(member);
        post.setId(postId);
        return post;

    }

}
