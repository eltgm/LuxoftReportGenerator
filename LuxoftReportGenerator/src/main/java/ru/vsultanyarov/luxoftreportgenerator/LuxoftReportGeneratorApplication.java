package ru.vsultanyarov.luxoftreportgenerator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication//(exclude = {SecurityAutoConfiguration.class})
public class LuxoftReportGeneratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(LuxoftReportGeneratorApplication.class, args);
    }
}