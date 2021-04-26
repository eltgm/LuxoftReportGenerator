package ru.vsultanyarov.luxoftreportgenerator.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.scheduling.annotation.Scheduled;
import ru.vsultanyarov.luxoftreportgenerator.domain.JiraIssue;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class CacheService {
    private final JiraService jiraService;
    @Autowired
    private CacheService self;

    public CacheService(JiraService jiraService) {
        this.jiraService = jiraService;
    }

    @Cacheable("jiraIssues")
    public List<JiraIssue> getJiraIssues() {
        return jiraService.getJiraIssues(new ArrayList<>(), "");
    }

    /**
     * Clear jira cache
     */
    @CacheEvict(value = "jiraIssues", allEntries = true)
    public void clearJiraIssues() {
        log.info("Clear cache");
    }

    /**
     * Scheduled refreshing caches
     */
    @Scheduled(cron = "${cache.refresh-rate}", zone = "Europe/Moscow")
    public void refreshCaches() {
        log.info("Refresh caches as per schedule");
        self.clearJiraIssues();
    }
}
