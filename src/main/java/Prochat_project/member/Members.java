package Prochat_project.member;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.util.Collection;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Members implements UserDetails {
   private Long id;
   private String memberId;
   private String memberPw;
   private String memberEmail;
   private MemberRole memberRole;

   private String memberName;
   private String memberPhone;
   private String memberAddress;
   private String memberAddressDetail;
   private String memberProfile;
   private String memberImage;

   private Timestamp regDate;
   private Timestamp updateDate;
   private Timestamp removeDate;




   public static Members fromEntity(MemberEntity entity){
      return new Members(
              entity.getId(),
              entity.getMemberId(),
              entity.getMemberPw(),
              entity.getMemberEmail(),
              entity.getRole(),
              entity.getMemberName(),
              entity.getMemberPhone(),
              entity.getMemberAddress(),
              entity.getMemberAddressDetail(),
              entity.getMemberProfile(),
              entity.getMemberImage(),
              entity.getRegDate(),
              entity.getUpdateDate(),
              entity.getRemoveDate()
      );
   }


   @Override
   @JsonIgnore
   public Collection<? extends GrantedAuthority> getAuthorities() {
      return null;
   }

   @Override
   @JsonIgnore
   public String getPassword() {
      return memberPw;
   }

   @Override
   @JsonIgnore
   public String getUsername() {
      return memberId;
   }

   @Override
   @JsonIgnore
   public boolean isAccountNonExpired() {
      return removeDate == null;
   }

   @Override
   @JsonIgnore
   public boolean isAccountNonLocked() {
      return removeDate == null;
   }

   @Override
   @JsonIgnore
   public boolean isCredentialsNonExpired() {
      return removeDate == null;
   }

   @Override
   @JsonIgnore
   public boolean isEnabled() {
      return removeDate == null;
   }
}