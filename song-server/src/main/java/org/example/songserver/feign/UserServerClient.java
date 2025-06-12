package org.example.songserver.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "backstage-user-server", url = "https://localhost:9002")
public interface UserServerClient {

    @PostMapping("/api/user/deleteUserSongRelation")
    void deleteUserSongRelation(@RequestBody List<Long> songIds);
}
