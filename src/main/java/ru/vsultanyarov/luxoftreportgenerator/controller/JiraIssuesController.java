package ru.vsultanyarov.luxoftreportgenerator.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;
import ru.vsultanyarov.luxoftreportgenerator.dto.JiraIssueDto;
import ru.vsultanyarov.luxoftreportgenerator.service.CacheService;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.springframework.util.StringUtils.hasText;

@RestController
public class JiraIssuesController {
    private final CacheService cacheService;

    public JiraIssuesController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    @GetMapping("/jiraIssues")
    public List<JiraIssueDto> getJiraIssues(@RequestParam(value = "q", required = false) String query) {
        if (!hasText(query)) {
            return cacheService.getJiraIssues().stream()
                    .limit(15)
                    .map(this::mapToStateItem)
                    .collect(toList());
        }

        return cacheService.getJiraIssues().stream()
                .filter(jiraIssue -> jiraIssue.getTaskNumber()
                        .toLowerCase()
                        .contains(query))
                .limit(15)
                .map(this::mapToStateItem)
                .collect(toList());
    }

    private JiraIssueDto mapToStateItem(JiraIssue jiraIssue) {
        return JiraIssueDto.builder()
                .id(jiraIssue.getTaskNumber())
                .text(jiraIssue.getTaskNumber())
                .slug(jiraIssue.getTaskNumber())
                .build();
    }
}
