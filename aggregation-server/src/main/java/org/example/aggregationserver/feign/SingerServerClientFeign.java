package org.example.aggregationserver.feign;

import org.example.commoncore.entity.dto.HomeDataList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "backstage-singer-server", url = "https://localhost:9005")
public interface SingerServerClientFeign {

    @GetMapping("/api/singer/getSingerSum")
    Integer getSingerSum();

    @GetMapping("/api/singer/getSingerSexList")
    HomeDataList[] getSingerSexList();

    @GetMapping("/api/singer/getSingerNationalityList")
    HomeDataList[] getSingerNationalityList();
}
