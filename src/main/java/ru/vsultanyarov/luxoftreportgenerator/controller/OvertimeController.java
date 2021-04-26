package ru.vsultanyarov.luxoftreportgenerator.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;
import ru.vsultanyarov.luxoftreportgenerator.service.OvertimeService;

import java.security.Principal;

@Controller
public class OvertimeController {
    private final OvertimeService overtimeService;

    public OvertimeController(OvertimeService overtimeService) {
        this.overtimeService = overtimeService;
    }

    @PostMapping("/overtimes")
    public String saveOvertime(JiraIssue jiraIssue, Principal principal) {
        overtimeService.updateOvertimes(jiraIssue, principal.getName());

        return "redirect:/index";
    }
}
