package ru.vsultanyarov.luxoftreportgenerator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "jira")
public class JiraProperties {
    /**
     * Хост jira
     */
    @NotNull
    private String host;

    /**
     * Логин пользоавтеля Jira, для которого составлять отчёт
     */
    @NotNull
    private String username;

    /**
     * Пароль пользователя Jira, для которого составлять отчёт
     */
    @NotNull
    private String password;
}