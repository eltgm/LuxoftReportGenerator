package ru.vsultanyarov.luxoftreportgenerator.domain;

import lombok.Value;

@Value
public class Overwork {
    double weekend;
    double weekday;
    String tasks;
}
