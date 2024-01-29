package Prochat_project.controller;

import Prochat_project.model.alarm.AlarmArgs;
import Prochat_project.model.alarm.AlarmProducer;
import Prochat_project.model.alarm.AlarmType;
import Prochat_project.model.entity.MemberEntity;
import Prochat_project.repository.MemberRepository;
import Prochat_project.service.AlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api-dev/v1")
@RequiredArgsConstructor
public class DevController {

    private final AlarmService notificationService;
    private final MemberRepository memberRepository;
    private final AlarmProducer alarmProducer;

    @GetMapping("/notification")
    public void test() {
        MemberEntity entity = memberRepository.findById(5L).orElseThrow();
        notificationService.send(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), entity.getId());
    }
//
//    @GetMapping("/send")
//    public void send() {
//        alarmProducer.send(new AlarmEvent(AlarmType.NEW_LIKE_ON_POST, new AlarmArgs(0, 0), 5));
//    }
}
