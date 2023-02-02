package com.devinho.pricefetcher.service;

import com.devinho.pricefetcher.model.dto.ScrapedProductDto;
import com.devinho.pricefetcher.model.dto.alert.EmailAlertDto;
import com.sendgrid.*;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class EmailService {

    private final SendGrid sendGrid;

    @Value("${sendgrid-config.from}")
    private String fromEmail;

    private static final String CONTENT_TYPE = "text/html";
    private static String priceChangeEmailSubjectPattern = "Price Fetcher Alert, %s";
    private static String priceChangeEmailContentPattern = "<p>Alert name: %s</p>" +
                                                           "<p>Old price: %s</p>" +
                                                           "<p>New price: %s</p>" +
                                                           "<p>URL: <a href=\"%s\">Product Link</a>";
    private static String alertCreateEmailSubjectPattern = "Price Fetcher - New alert (%s)";
    private static String alertCreateEmailContentPattern = "<p>New alert created and assigned to e-mail address: %s</p>" +
                                                           "<p>Alert name: %s</p>" +
                                                           "<p>URL: <a href=\"%s\">Product Link</a>" +
                                                           "<p>E-commerce: %s</p>";

    public void sendEmailWithAlertCreateConfirmation(EmailAlertDto emailAlertDto) {
        var mail = createNewAlertCreateMail(emailAlertDto);
        var request = setupRequest(mail);
        sendEmailMessage(request, emailAlertDto.email());
    }

    public LocalDateTime sendEmailWithPriceChangeInfo(EmailAlertDto emailAlertDto, ScrapedProductDto scrapedProductDto, Double oldPrice) {
        var mail = createNewPriceChangeMail(emailAlertDto, scrapedProductDto, oldPrice);
        var request = setupRequest(mail);
        return sendEmailMessage(request, emailAlertDto.email());
    }

    private LocalDateTime sendEmailMessage(Request request, String receiver) {
        try {
            var response = sendGrid.api(request);
            log.info("E-mail message [status: {}, receiver: {}]", response.getStatusCode(), receiver);
            return LocalDateTime.now();
        } catch (IOException e) {
            throw new RuntimeException("Cannot send e-mail message. " + e.getMessage());
        }
    }

    private Request setupRequest(Mail mail) {
        try {
            var request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            return request;
        } catch (IOException e) {
            throw new RuntimeException("Cannot build e-mail request. " + e.getMessage());
        }
    }

    private Mail createNewPriceChangeMail(EmailAlertDto emailAlertDto, ScrapedProductDto scrapedProductDto, Double oldPrice) {
        var price = scrapedProductDto.price();
        var from = new Email(fromEmail);
        var to = new Email(emailAlertDto.email());
        var subject = String.format(priceChangeEmailSubjectPattern, emailAlertDto.alertName());
        var currency = price.currency().value;
        var strContent = String.format(priceChangeEmailContentPattern,
                emailAlertDto.alertName(),
                oldPrice + currency,
                price.value() + currency,
                scrapedProductDto.url());
        var content = new Content(CONTENT_TYPE, strContent);
        return new Mail(from, subject, to, content);
    }

    private Mail createNewAlertCreateMail(EmailAlertDto emailAlertDto) {
        var from = new Email(fromEmail);
        var to = new Email(emailAlertDto.email());
        var subject = String.format(alertCreateEmailSubjectPattern, emailAlertDto.alertName());
        var strContent = String.format(alertCreateEmailContentPattern,
                emailAlertDto.email(),
                emailAlertDto.alertName(),
                emailAlertDto.productUrl(),
                emailAlertDto.ecommerce());
        var content = new Content(CONTENT_TYPE, strContent);
        return new Mail(from, subject, to, content);
    }
}
