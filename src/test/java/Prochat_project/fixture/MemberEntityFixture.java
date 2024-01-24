package Prochat_project.fixture;

import Prochat_project.model.entity.MemberEntity;

public class MemberEntityFixture {

    public static MemberEntity get(String memberId, String memberPw){
       MemberEntity result = new MemberEntity();
       result.setId(1);
       result.setMemberId(memberId);
       result.setMemberPw(memberPw);
       return result;

    }
}
