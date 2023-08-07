package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.analytics.AverageSalaryData;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/analytics")
public class AnalyticsRestController {

    private final RestApiClientService service;

    public AnalyticsRestController(RestApiClientService service) {
        this.service = service;
    }

    @GetMapping("/average-salary/{query}")
    public List<AverageSalaryData> averageSalary(@PathVariable String query, @RequestParam("depth") String depth) {
        try {
            return service.averageSalaryByQuery(query, depth);
        } catch (RestApiException e) {
            return Collections.emptyList();
        }
    }
}
