package Prochat_project.model;


import Prochat_project.model.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.sql.Timestamp;

@Getter
@AllArgsConstructor
public class Members {
   private Integer id;
   private String memberId;
   private String memberPw;
   private MemberRole memberRole;
   private Timestamp regDate;
   private Timestamp updateDate;
   private Timestamp deleteDate;




   public static Members fromEntity(MemberEntity entity){
      return new Members(
              entity.getId(),
              entity.getMemberId(),
              entity.getMemberPw(),
              entity.getRole(),
              entity.getRegDate(),
              entity.getUpdateDate(),
              entity.getDeleteDate()
      );
   }
}