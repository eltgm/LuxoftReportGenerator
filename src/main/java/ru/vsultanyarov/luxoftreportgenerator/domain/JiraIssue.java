package ru.vsultanyarov.luxoftreportgenerator.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JiraIssue implements Serializable {
    private String taskNumber;
    private String taskTitle;
    private String taskStatus;
    private Double overtime;
    private Boolean isWeekend;
}