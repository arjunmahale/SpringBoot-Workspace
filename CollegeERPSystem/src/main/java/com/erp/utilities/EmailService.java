package com.erp.utilities;
import com.sendgrid.*;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    @Value("${sendgrid.api.key}")
    private String sendGridApiKey;

    public void sendRegistrationMail(String toEmail, String name) {
        Email from = new Email("maheshmahale588@gmail.com"); // your verified sender
        String subject = "Registration Successful!";
        Email to = new Email(toEmail);
        String body = "Hello " + name + ",\n\n" +
                "Your registration was successful! You can now log in using your registered credentials.\n\n" +
                "Thank you,\nCareer Guidance Team\n\n\n"
                + "Username : "+name
                + "\nPassword : *********** (Your Mobile Number)";

        Content content = new Content("text/plain", body);
        Mail mail = new Mail(from, subject, to, content);

        SendGrid sg = new SendGrid(sendGridApiKey);
        Request request = new Request();

        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            sg.api(request);
            System.out.println("✅ Confirmation email sent to " + toEmail);
        } catch (Exception e) {
            System.out.println("⚠️ Failed to send email: " + e.getMessage());
        }
    }
}
