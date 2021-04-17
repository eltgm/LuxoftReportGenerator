package ru.vsultanyarov.luxoftreportgenerator.service;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import org.springframework.stereotype.Service;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class JiraService {
    private final JiraRestClient jiraRestClient;

    public JiraService(JiraRestClient jiraRestClient) {
        this.jiraRestClient = jiraRestClient;
    }

    public List<JiraIssue> getJiraIssues(List<JiraIssue> jiraIssues, String excludedTaskKeys) {
        List<JiraIssue> foundIssues = StreamSupport.stream(jiraRestClient.getSearchClient()
                .searchJql("assignee = currentUser() AND updatedDate >= startOfMonth(0)" + excludedTaskKeys)
                .claim()
                .getIssues().spliterator(), false)
                .map(issue -> new JiraIssue(issue.getKey(), issue.getSummary(), issue.getStatus().getName().toUpperCase()))
                .collect(Collectors.toList());

        jiraIssues.addAll(foundIssues);
        if (foundIssues.size() >= 50) {
            return getJiraIssues(jiraIssues, generateExcludedIssuesRequest(foundIssues));
        }

        return jiraIssues;
    }

    private String generateExcludedIssuesRequest(List<JiraIssue> foundIssues) {
        return " AND issueKey not in " + foundIssues.stream()
                .map(JiraIssue::getTaskNumber)
                .collect(Collectors.joining(",", "(", ")"));
    }
}