package ru.vsultanyarov.luxoftreportgenerator.service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import org.springframework.stereotype.Service;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;

import java.util.List;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

@Service
public class JiraService {
    private final JiraRestClient jiraRestClient;

    public JiraService(JiraRestClient jiraRestClient) {
        this.jiraRestClient = jiraRestClient;
    }

    public List<JiraIssue> getJiraIssues(List<JiraIssue> jiraIssues, String excludedTaskKeys) {
        List<JiraIssue> foundIssues = stream(jiraRestClient.getSearchClient()
                .searchJql("assignee = currentUser() AND updatedDate >= startOfMonth(0)" + excludedTaskKeys)
                .claim()
                .getIssues().spliterator(), false)
                .map(issue -> JiraIssue.builder()
                        .taskNumber(issue.getKey())
                        .taskTitle(issue.getSummary())
                        .taskStatus(issue.getStatus().getName().toUpperCase())
                        .build())
                .collect(toList());

        jiraIssues.addAll(foundIssues);
        if (foundIssues.size() >= 50) {
            return getJiraIssues(jiraIssues, generateExcludedIssuesRequest(foundIssues));
        }

        return jiraIssues;
    }

    private String generateExcludedIssuesRequest(List<JiraIssue> foundIssues) {
        return " AND issueKey not in " + foundIssues.stream()
                .map(JiraIssue::getTaskNumber)
                .collect(joining(",", "(", ")"));
    }
}