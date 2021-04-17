package ru.vsultanyarov.luxoftreportgenerator.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@Validated
@ConfigurationProperties(prefix = "report")
public class ReportProperties {
    /**
     * Имя пользователя, для которого будет генерироваться отчёт
     */
    @NotNull
    private String username;

    /**
     * Почта получателя отчёта.
     */
    @NotNull
    private String email;

    /**
     * Крон с датой генрации отчёта
     */
    @NotNull
    private String generate;

    /**
     * Конфигурация шаблона
     */
    @NotNull
    private Template template;

    @Getter
    @Setter
    @Validated
    public static class Template {
        /**
         * Путь до шаблона
         */
        @NotNull
        private String path;

        /**
         * Название шаблона
         */
        @NotNull
        private String name;
    }
}