package org.example.songserver.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "backstage-user-server", url = "https://localhost:9002")
public interface UserServerClient {

    @GetMapping("/api/user/deleteUserSongRelation")
    void deleteUserSongRelation(List<Long> songIds);
}
