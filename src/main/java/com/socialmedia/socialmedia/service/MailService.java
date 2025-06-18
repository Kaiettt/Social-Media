package com.socialmedia.socialmedia.service;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
public class MailService {
    private final JavaMailSender mailSender;
    
    @Async
    public void send(String to, String userName, long token) {
        String emailContent = buildEmail(userName,token);
        try {

            MimeMessage mimeMessage = mailSender.createMimeMessage();

            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");

            helper.setText(emailContent, true);

            helper.setTo(to);

            helper.setSubject("Confirm your email.");

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {


            throw new IllegalStateException("failed to send email");
        }
    }

    public String buildEmail(String name, long token) {
        return "<div style=\"font-family:Helvetica,Arial,sans-serif;font-size:16px;margin:0;color:#0b0c0c\">\n" +
                "<span style=\"display:none;font-size:1px;color:#fff;max-height:0\"></span>\n" +
                "<table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;min-width:100%;width:100%!important\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\">\n" +
                "    <tbody><tr>\n" +
                "        <td width=\"100%\" height=\"53\" bgcolor=\"#0b0c0c\">\n" +
                "            <table role=\"presentation\" width=\"100%\" style=\"border-collapse:collapse;max-width:580px\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"center\">\n" +
                "                <tbody><tr>\n" +
                "                    <td width=\"70\" bgcolor=\"#0b0c0c\" valign=\"middle\">\n" +
                "                        <table role=\"presentation\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse\">\n" +
                "                            <tbody><tr>\n" +
                "                                <td style=\"padding-left:10px\"></td>\n" +
                "                                <td style=\"font-size:28px;line-height:1.315789474;Margin-top:4px;padding-left:10px\">\n" +
                "                                    <span style=\"font-family:Helvetica,Arial,sans-serif;font-weight:700;color:#ffffff;text-decoration:none;vertical-align:top;display:inline-block\">Confirm your email</span>\n" +
                "                                </td>\n" +
                "                            </tr>\n" +
                "                        </tbody></table>\n" +
                "                    </td>\n" +
                "                </tr>\n" +
                "            </tbody></table>\n" +
                "        </td>\n" +
                "    </tr>\n" +
                "</tbody></table>\n" +
                "<table role=\"presentation\" align=\"center\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" style=\"border-collapse:collapse;max-width:580px;width:100%!important\" width=\"100%\">\n" +
                "    <tbody><tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "        <td style=\"font-family:Helvetica,Arial,sans-serif;font-size:19px;line-height:1.315789474;max-width:560px\">\n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Hi " + name + ",</p>\n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Thank you for registering. Use the following verification code to activate your account:</p>\n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:22px;font-weight:bold;line-height:25px;color:#1D70B8;text-align:center\">" + token + "</p>\n" +
                "            <p style=\"Margin:0 0 20px 0;font-size:19px;line-height:25px;color:#0b0c0c\">Token will expire in 15 minutes.</p>\n" +
                "            <p>See you soon!</p>\n" +
                "        </td>\n" +
                "        <td width=\"10\" valign=\"middle\"><br></td>\n" +
                "    </tr>\n" +
                "    <tr>\n" +
                "        <td height=\"30\"><br></td>\n" +
                "    </tr>\n" +
                "</tbody></table>\n" +
                "<div class=\"yj6qo\"></div><div class=\"adL\"></div></div>";
    }
}
