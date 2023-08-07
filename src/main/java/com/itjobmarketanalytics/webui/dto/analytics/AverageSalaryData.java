package com.itjobmarketanalytics.webui.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AverageSalaryData {
    private LocalDate date;
    private int vacancyCount;
    private double salary;
}
