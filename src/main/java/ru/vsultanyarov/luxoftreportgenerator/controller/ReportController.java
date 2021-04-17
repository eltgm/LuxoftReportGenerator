package ru.vsultanyarov.luxoftreportgenerator.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vsultanyarov.luxoftreportgenerator.domain.ReportDataInfo;
import ru.vsultanyarov.luxoftreportgenerator.domain.User;
import ru.vsultanyarov.luxoftreportgenerator.dto.ReportDateDto;
import ru.vsultanyarov.luxoftreportgenerator.service.WorkCalendarService;

import java.security.Principal;

@Controller
public class ReportController {
    private final WorkCalendarService workCalendarService;

    public ReportController(WorkCalendarService workCalendarService) {
        this.workCalendarService = workCalendarService;
    }

    @GetMapping(path = {"/", "/index"})
    public String reportDateInfo(Principal principal, Model model) {
        ReportDataInfo reportDataInfo = workCalendarService.getReportData(principal.getName());

        model.addAttribute("reportDataInfo", reportDataInfo);
        model.addAttribute("reportDate", new ReportDateDto());
        return "index";
    }

    @PostMapping("/missingDays")
    public String updateMissingDays(ReportDateDto reportDate, Model model, Principal principal) {
        workCalendarService.updateMissedDays(User.builder()
                .missedDays(reportDate.getMissedDays())
                .username(principal.getName())
                .build());

        return "redirect:/index";
    }
}