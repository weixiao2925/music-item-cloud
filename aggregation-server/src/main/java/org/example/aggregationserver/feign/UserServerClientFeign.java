package org.example.aggregationserver.feign;

import org.example.commoncore.entity.dto.HomeDataList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "backstage-user-server", url = "https://localhost:9002")
public interface UserServerClientFeign {

    @GetMapping("/api/user/getUserSum")
    Integer getUserSum();

    @GetMapping("/api/user/getUserSexList")
    HomeDataList[] getUserSexList();

}
