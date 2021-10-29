package ru.vsultanyarov.luxoftreportgenerator.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class ReportGeneratorAndSender {
    private final ReportGeneratorService reportGeneratorService;

    public ReportGeneratorAndSender(ReportGeneratorService reportGeneratorService) {
        this.reportGeneratorService = reportGeneratorService;
    }

    @Scheduled(cron = "${report.generate}", zone = "Europe/Moscow")
    public void sendReport() {
        reportGeneratorService.startReportGeneration();
    }
}