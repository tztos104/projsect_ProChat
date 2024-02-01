package Prochat_project.service;

import Prochat_project.exception.ErrorCode;
import Prochat_project.exception.ProchatException;
import Prochat_project.model.alarm.AlarmArgs;
import Prochat_project.model.alarm.AlarmType;
import Prochat_project.model.entity.FollowEntity;
import Prochat_project.member.MemberEntity;
import Prochat_project.repository.FollowRepository;
import Prochat_project.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowService {

    private final FollowRepository followRepository;
    private final MemberRepository memberRepository;
    private final AlarmService alarmService;

    //지정된 사용자의 팔로워 목록을 가져오는 메서드.
    public List<FollowEntity> getFollowers(String memberId) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", memberId)));
        return followRepository.findAllByFollower(memberEntity);
    }
    // 지정된 사용자가 팔로우 중인 목록을 가져오는 메서드.
    public List<FollowEntity> getFollowing(String memberId) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", memberId)));
        return followRepository.findAllByFollowing(memberEntity);
    }
    //팔로우
    public void follow(String follower, String following) {
        if (follower.equals(following)) {
            throw new ProchatException(ErrorCode.INVALID_FOLLOW, "자기 자신을 팔로우할 수 없습니다");
        }
        MemberEntity followerId = memberRepository.findByMemberId(follower).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", follower)));
        MemberEntity followingById = memberRepository.findByMemberId(following).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", follower)));

        followRepository.findByFollowerAndFollowing(followerId, followingById).ifPresent(it -> {
                    throw new ProchatException(ErrorCode.DUPLICATED_FOLLOW, String.format("%s 는 %s 를 이미 팔로우 하고 있습니다", follower, following));
                });
        followRepository.save(FollowEntity.of(followerId, followingById));
        String content = String.format("%s 님에게 팔로우 요청이 들어왔습니다",followerId.getMemberId());
        alarmService.send(AlarmType.NEW_FOLLOW, new AlarmArgs(followerId.getMemberId(), followingById.getId()), followingById.getId(),content);

    }

    public void unfollow(String follower, String following) {
        MemberEntity followerId = memberRepository.findByMemberId(follower).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", follower)));
        MemberEntity followingById = memberRepository.findByMemberId(following).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", follower)));
        FollowEntity followEntity = followRepository.findByFollowerAndFollowing(followerId, followingById).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 는 %s 를 이미 팔로우 하고 있습니다", follower, following)));
        followRepository.delete(followEntity);
    }
    public Integer getFollowCount(String memberId) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", memberId)));
        List<FollowEntity> allByFollower = followRepository.findAllByFollower(memberEntity);

        return allByFollower.size();
    }
    public Integer getFollowingCount(String memberId) {
        MemberEntity memberEntity = memberRepository.findByMemberId(memberId).orElseThrow(() ->
                new ProchatException(ErrorCode.USER_NOT_FOUND, String.format("%s 유저를 찾을 수 없습니다", memberId)));
        List<FollowEntity> allByFollower = followRepository.findAllByFollowing(memberEntity);

        return allByFollower.size();
    }

}
