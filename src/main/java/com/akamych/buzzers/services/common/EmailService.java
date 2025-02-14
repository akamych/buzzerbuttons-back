package com.akamych.buzzers.services.common;

import com.akamych.buzzers.entities.stats.StatsDaily;
import com.akamych.buzzers.services.stats.StatsDailyService;
import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import com.sendgrid.helpers.mail.objects.Content;
import com.sendgrid.helpers.mail.objects.Email;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class EmailService {
    @Value("${BUZZERS_REPORTING_EMAIL}")
    private String REPORT_EMAIL;
    private final StatsDailyService statsDailyService;

    @Value("${sendgrid.api-key}")
    private String sendGridApiKey;

    public void sendEmail(String to, String subject, String content) {
        sendEmail(to, subject, content, false);
    }

    public void sendEmail(String to, String subject, String content, boolean isHTML) {
        Email from = new Email(REPORT_EMAIL);
        Email toEmail = new Email(to);
        Content emailContent = new Content("text/plain", content);
        Mail mail = new Mail(from, subject, toEmail, emailContent);

        SendGrid sendGrid = new SendGrid(sendGridApiKey);
        Request request = new Request();
        try {
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());
            Response response = sendGrid.api(request);

            if (response.getStatusCode() != 200) {
                //TODO: logging
                System.out.println("Response Code: " + response.getStatusCode());
                System.out.println("Response Body: " + response.getBody());
                System.out.println("Response Headers: " + response.getHeaders());
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("email not sent");
        }
    }


    @Scheduled(fixedRate = 86400000)
    @Transactional
    public void sendDailyStats() {

        StatsDaily yesterdayStats = statsDailyService.getYesterdayStatsDaily();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy/MM/dd");

        sendEmail(
                REPORT_EMAIL,
                "DAILY STATS: BUZZERS: " + yesterdayStats.getDate().format(formatter),
                yesterdayStats.getDate().format(formatter) + "\n" +
                        "Games: " + yesterdayStats.getGames() + "\n" +
                        "Games activated: " + yesterdayStats.getGamesActivated() + "\n" +
                        "Additional Rounds: " + yesterdayStats.getRounds() + "\n" +
                        "Hosts: " + yesterdayStats.getHosts() + "\n" +
                        "Players: " + yesterdayStats.getPlayers() + "\n" +
                        "Buttons pressed: " + yesterdayStats.getButtonsPressed()
        );
    }

}
