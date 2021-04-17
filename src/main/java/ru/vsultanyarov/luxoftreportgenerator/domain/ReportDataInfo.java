package ru.vsultanyarov.luxoftreportgenerator.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportDataInfo {
    private int workDays;
    private int daysOff;
    private int missedDays;
    private int reportGenerateDay;
    private String reportGenerateMonth;
}