package ru.vsultanyarov.luxoftreportgenerator.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import ru.vsultanyarov.luxoftreportgenerator.service.EmailService;

import static java.lang.String.format;

@Component
@Aspect
public class ExceptionLogAdvice {
    private final EmailService emailService;

    public ExceptionLogAdvice(EmailService emailService) {
        this.emailService = emailService;
    }

    @AfterThrowing(pointcut = "execution(* ru.vsultanyarov.luxoftreportgenerator.service..*.*(..))", throwing = "ex")
    public void logException(JoinPoint joinPoint, Throwable ex) {
        emailService.sendError(format("Приключилась беда в методе %s.%n Сообщение %s", joinPoint.getSignature(), ex.getLocalizedMessage()));
    }
}