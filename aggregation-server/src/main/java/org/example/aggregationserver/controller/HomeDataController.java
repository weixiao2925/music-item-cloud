package org.example.aggregationserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.aggregationserver.service.HomeDataService;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.vo.response.HoneDataSumVO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/home")
@RequiredArgsConstructor
public class HomeDataController {

    private final HomeDataService homeDataService;

    @GetMapping("/getHomeData")
    public RestBean<HoneDataSumVO> getHomeData() {
        return RestBean.success(homeDataService.getHomeDataList());
    }

}
