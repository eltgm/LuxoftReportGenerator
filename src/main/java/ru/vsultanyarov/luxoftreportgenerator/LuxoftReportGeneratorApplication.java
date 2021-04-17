package ru.vsultanyarov.luxoftreportgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
public class LuxoftReportGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuxoftReportGeneratorApplication.class, args);
    }
}