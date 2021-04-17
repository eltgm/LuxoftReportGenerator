package ru.vsultanyarov.luxoftreportgenerator.service;

import lombok.SneakyThrows;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.vsultanyarov.luxoftreportgenerator.config.ReportProperties;
import ru.vsultanyarov.luxoftreportgenerator.dao.UserDao;
import ru.vsultanyarov.luxoftreportgenerator.domain.User;

import javax.mail.internet.MimeMessage;
import java.io.File;

@Service
public class EmailService {
    private final JavaMailSender emailSender;
    private final ReportProperties reportProperties;
    private final UserDao userDao;

    public EmailService(JavaMailSender javaMailSender, ReportProperties reportProperties, UserDao userDao) {
        this.emailSender = javaMailSender;
        this.reportProperties = reportProperties;
        this.userDao = userDao;
    }

    @SneakyThrows
    public void sendReport(String reportName) {
        String email = reportProperties.getEmail();
        MimeMessage message = emailSender.createMimeMessage();

        var helper = new MimeMessageHelper(message, true);

        helper.setFrom(email);
        helper.setTo(email);
        helper.setSubject("Отчёт");

        var reportFile = new File(reportName);
        var report = new FileSystemResource(reportFile);
        helper.addAttachment(reportName, report);
        helper.setText("", true);

        emailSender.send(message);
        reportFile.delete();
        User user = userDao.getUser(reportProperties.getUsername());
        user.setMissedDays(0);
        userDao.updateUserMissedDays(user);
    }

    public void sendError(String error) {
        String email = reportProperties.getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(email);
        message.setSubject("Генерация отчёта. Ошибка");
        message.setText(error);
        emailSender.send(message);
    }
}