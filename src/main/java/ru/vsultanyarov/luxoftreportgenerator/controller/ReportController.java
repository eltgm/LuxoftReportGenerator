package ru.vsultanyarov.luxoftreportgenerator.controller;


import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;
import ru.vsultanyarov.luxoftreportgenerator.domain.Overwork;
import ru.vsultanyarov.luxoftreportgenerator.domain.ReportDataInfo;
import ru.vsultanyarov.luxoftreportgenerator.domain.User;
import ru.vsultanyarov.luxoftreportgenerator.dto.ReportDateDto;
import ru.vsultanyarov.luxoftreportgenerator.service.OvertimeService;
import ru.vsultanyarov.luxoftreportgenerator.service.WorkCalendarService;

import java.security.Principal;

@Controller
public class ReportController {
    private final WorkCalendarService workCalendarService;
    private final OvertimeService overtimeService;

    public ReportController(WorkCalendarService workCalendarService,
                            OvertimeService overtimeService) {
        this.workCalendarService = workCalendarService;
        this.overtimeService = overtimeService;
    }

    @GetMapping(path = {"/", "/index"})
    public String reportDateInfo(Principal principal, Model model) {
        String principalName = principal.getName();
        ReportDataInfo reportDataInfo = workCalendarService.getReportData(principalName);
        Overwork overwork = overtimeService.getOverwork(principalName);

        model.addAttribute("reportDataInfo", reportDataInfo);
        model.addAttribute("reportDate", new ReportDateDto(0));
        model.addAttribute("jiraIssue", new JiraIssue(null, null, null, 0.5, false));
        model.addAttribute("overwork", overwork);
        return "index";
    }

    @PostMapping("/missingDays")
    public String updateMissingDays(ReportDateDto reportDate, Principal principal) {
        workCalendarService.updateMissedDays(User.builder()
                .missedDays(reportDate.getMissedDays())
                .username(principal.getName())
                .build());

        return "redirect:/index";
    }
}