package ru.vsultanyarov.luxoftreportgenerator.config;

import com.atlassian.jira.rest.client.api.JiraRestClient;
import com.atlassian.jira.rest.client.internal.async.AsynchronousJiraRestClientFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.URI;

@Configuration
public class JiraClientConfiguration {
    private final JiraProperties jiraProperties;

    public JiraClientConfiguration(JiraProperties jiraProperties) {
        this.jiraProperties = jiraProperties;
    }

    @Bean
    JiraRestClient jiraRestClient() {
        return new AsynchronousJiraRestClientFactory()
                .createWithBasicHttpAuthentication(URI.create(jiraProperties.getHost()), jiraProperties.getUsername(), jiraProperties.getPassword());
    }
}