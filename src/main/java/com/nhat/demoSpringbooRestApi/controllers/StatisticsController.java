package com.nhat.demoSpringbooRestApi.controllers;

import com.nhat.demoSpringbooRestApi.dtos.BaseResponse;
import com.nhat.demoSpringbooRestApi.dtos.StatisticResponseDTO;
import com.nhat.demoSpringbooRestApi.services.StatisticsService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/statistics")
public class StatisticsController {

    private static final Logger logger = Logger.getLogger(StatisticsController.class);
    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/count-item")
    public ResponseEntity<BaseResponse> getStatisticsShop() throws Exception {
        StatisticResponseDTO statisticResponseDTO = statisticsService.getStatisticsShop();
        BaseResponse baseResponse = new BaseResponse(true, "getStatisticsShop.success.getAll", statisticResponseDTO, null);
        return ResponseEntity.status(200).body(baseResponse);
    }

    @GetMapping("/revenue-by-months")
    public ResponseEntity<BaseResponse> getRevenueByMonths(@RequestParam(name = "year", required = true) Integer year) throws Exception {
        List<Double> revenueByMonths = statisticsService.getRevenueByMonths(year);
        BaseResponse baseResponse = new BaseResponse(true, "getRevenueByMonths.success.getAll", revenueByMonths, null);
        return ResponseEntity.status(200).body(baseResponse);
    }

}
