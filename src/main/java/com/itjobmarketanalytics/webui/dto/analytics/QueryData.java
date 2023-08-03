package com.itjobmarketanalytics.webui.dto.analytics;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueryData {
    private String query;
    private int vacancyCount;
    private double averageSalary;
}
