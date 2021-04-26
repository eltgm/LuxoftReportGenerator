package ru.vsultanyarov.luxoftreportgenerator.config;

import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.vsultanyarov.luxoftreportgenerator.service.CacheService;
import ru.vsultanyarov.luxoftreportgenerator.service.JiraService;

@Configuration
@EnableCaching
public class CacheConfiguration {
    private final JiraService jiraService;

    public CacheConfiguration(JiraService jiraService) {
        this.jiraService = jiraService;
    }

    @Bean
    CacheService jiraIssuesCacheService() {
        return new CacheService(jiraService);
    }
}
