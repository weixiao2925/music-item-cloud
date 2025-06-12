package org.example.songserver.feign;

import org.example.commoncore.entity.vo.request.SongAddVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "backstage-singer-server", url = "https://localhost:9005")
public interface SingerServiceClient {
    /** 获取某个 singer 的歌曲总数 */
    @GetMapping("/api/singer/getSongSum")
    Integer getSongSum(@RequestParam("singer_id") int singerId);

    /** 分页获取歌曲 id 列表 */
    @GetMapping("/api/singer/getSongIdList")
    List<Integer> getSongIdList(@RequestParam("singer_id") int singerId,
                                @RequestParam("page")       int page,
                                @RequestParam("pageSize")   int pageSize);

    /** 判断 singer 是否存在 */
    @GetMapping("/api/singer/isExist")
    Integer isExist(@RequestParam("singer_id") Integer singerId);

    /** 新增 singer-song 关联 —— 写操作改 POST，但 URL 不变 */
    @PostMapping("/api/singer/addSSRelation")
    void addSSRelation(@RequestBody SongAddVO vo);

    /** 根据 singerId 查询所有歌曲 id */
    @GetMapping("/api/singer/getSongIdsBySingerId")
    List<Integer> getSongIdsBySingerId(@RequestParam("singer_id") int singerId);

    /** 批量删除关联 —— 写操作改 POST，URL 同名 */
    @PostMapping("/api/singer/deleteSingersSongRelation")
    void deleteSingersSongRelation(@RequestBody List<Long> songIds);
}
