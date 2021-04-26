package ru.vsultanyarov.luxoftreportgenerator.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class JiraIssueDto {
    private String id;
    private String text;
    private String slug;
}
