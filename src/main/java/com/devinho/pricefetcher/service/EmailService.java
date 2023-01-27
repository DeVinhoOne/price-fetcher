package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.entity.EmailAlert;
import com.sendgrid.*;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final SendGrid sendGrid;

    @Value("${sendgrid-config.from}")
    private String fromEmail;

    private static final String CONTENT_TYPE = "text/plain";
    private static String emailSubjectPattern = "Price Fetcher Alert, %s";
    private static String emailContentPattern = "Alert name: %s\n" +
                                                "Alert description: %s\n" +
                                                "Price: %s\n" +
                                                "URL: %s";

    public void send(EmailAlert emailAlert, ScrapedProductDto scrapedProductDto) {
        var price = scrapedProductDto.price();

        var from = new Email(fromEmail);
        var to = new Email(emailAlert.getEmail());

        var subject = String.format(emailSubjectPattern, emailAlert.getAlertName());
        var content = new Content(CONTENT_TYPE,
                String.format(emailContentPattern, emailAlert.getAlertName(), emailAlert.getAlertDescription(), price.value() + price.currency().value, scrapedProductDto.url()));
        var mail = new Mail(from, subject, to, content);
        var request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            var response = sendGrid.api(request);
            log.info("E-mail message [status: {}, receiver: {}]", response.getStatusCode(), to.getEmail());
        } catch (IOException ex) {
            log.error("E-mail message error [receiver: {}]" , to.getEmail());
        }
    }

}
