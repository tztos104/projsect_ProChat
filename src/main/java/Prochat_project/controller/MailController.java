package Prochat_project.controller;

import Prochat_project.member.request.EmailCertRequest;
import Prochat_project.member.response.EmailCertResponse;
import Prochat_project.controller.response.Response;
import Prochat_project.service.MailSendService;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/mails")
public class MailController {

    private final MailSendService mailSendService;


    @PostMapping("/send-certification")
    public Response<EmailCertResponse> sendCertificationNumber(@RequestBody EmailCertRequest request)
            throws MessagingException, NoSuchAlgorithmException {
        EmailCertResponse emailCertResponse = mailSendService.sendEmailForCertification(request.getEmail());
        return Response.success(emailCertResponse);
    }

    @GetMapping("/verify")
    public Response<Void> verifyCertificationNumber(
            @RequestParam(name = "email") String email,
            @RequestParam(name = "certificationNumber") String certificationNumber
    ) {
        mailSendService.verifyEmail(email, certificationNumber);
        return Response.success();
    }
}