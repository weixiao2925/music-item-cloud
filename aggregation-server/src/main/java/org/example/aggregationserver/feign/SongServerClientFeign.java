package org.example.aggregationserver.feign;

import org.example.commoncore.entity.dto.HomeDataList;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "backstage-song-server", url = "https://localhost:9004")
public interface SongServerClientFeign {

    @GetMapping("/api/song/getSongSum")
    Integer getSongSum();

    @GetMapping("/api/song/getSongTypeList")
    HomeDataList[] getSongTypeList();
}
