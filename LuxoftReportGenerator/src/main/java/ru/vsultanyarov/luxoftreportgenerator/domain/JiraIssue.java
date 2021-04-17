package ru.vsultanyarov.luxoftreportgenerator.domain;

import lombok.Value;

@Value
public class JiraIssue {
    String taskNumber;
    String taskTitle;
    String taskStatus;
}