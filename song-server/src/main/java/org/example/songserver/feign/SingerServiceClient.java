package org.example.songserver.feign;

import org.example.commoncore.entity.vo.request.SongAddVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "backstage-singer-server", url = "https://localhost:9005")
public interface SingerServiceClient {
    @GetMapping("/api/singer/getSongSum")
    Integer getSongSum(int singer_id);
    @GetMapping("/api/singer/getSongIdList")
    List<Integer> getSongIdList(int singer_id, int page, int pageSize);
    @GetMapping("/api/singer/isExist")
    Integer isExist(Integer singer_id);
    @GetMapping("/api/singer/addSSRelation")
    void addSSRelation(SongAddVO vo);
    @GetMapping("/api/singer/getSongIdsBySingerId")
    List<Integer> getSongIdsBySingerId(int singer_id);
    @GetMapping("/api/singer/deleteSingersSongRelation")
    void deleteSingersSongRelation(List<Long> songIds);
}
