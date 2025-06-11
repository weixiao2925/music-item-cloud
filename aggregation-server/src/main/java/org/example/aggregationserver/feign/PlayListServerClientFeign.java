package org.example.aggregationserver.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "backstage-playlist-server", url = "https://localhost:9003")
public interface PlayListServerClientFeign {

    @GetMapping("/api/playlist/getPlaylistSum")
    Integer getPlaylistSum();
}

