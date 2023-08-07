package com.itjobmarketanalytics.webui.controller;

import com.itjobmarketanalytics.webui.dto.UserSubscriptionsDto;
import com.itjobmarketanalytics.webui.dto.analytics.AverageSalaryData;
import com.itjobmarketanalytics.webui.exception.RestApiException;
import com.itjobmarketanalytics.webui.service.RestApiClientService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<AverageSalaryData> averageSalary(@PathVariable String query) {
        try {
            return service.averageSalaryByQuery(query);
        } catch (RestApiException e) {
            return Collections.emptyList();
        }
    }

    @GetMapping("/allAvailable")
    public List<UserSubscriptionsDto> allAvailable() {
        try {
            return service.getAvailableSubscriptions();
        }catch (RestApiException e) {
            return Collections.emptyList();
        }
    }
}
