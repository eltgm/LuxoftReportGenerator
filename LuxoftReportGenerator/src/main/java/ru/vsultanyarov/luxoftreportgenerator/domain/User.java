package ru.vsultanyarov.luxoftreportgenerator.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
    private String username;
    private int missedDays;
}