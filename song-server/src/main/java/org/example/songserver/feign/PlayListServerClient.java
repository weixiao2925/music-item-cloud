package org.example.songserver.feign;

import org.example.commoncore.entity.vo.request.SongAdd_PVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "backstage-playlist-server", url = "https://localhost:9003")
public interface PlayListServerClient {

    /** 播放列表中歌曲总数 —— 查询类 */
    @GetMapping("/api/playlist/getSongsSumP")
    Integer getSongsSumP(@RequestParam("playlist_id") int playlistId);

    /** 分页获取歌曲 id —— 查询类 */
    @GetMapping("/api/playlist/getSongIdList")
    List<Integer> getSongIdList(@RequestParam("playlist_id") int playlistId,
                                @RequestParam("page")        int page,
                                @RequestParam("pageSize")    int pageSize);

    /** 判断 playlist 是否存在 —— 查询类 */
    @GetMapping("/api/playlist/isExistPlaylist")
    Integer isExistPlaylist(@RequestParam("playlist_id") int playlistId);

    /** 根据关键字获取歌曲数据 —— 查询类 */
    @GetMapping("/api/playlist/getSongDataByKeyNameP")
    List<Integer> getSongDataByKeyNameP(@RequestParam("playlist_id") int playlistId);

    /** 判断指定歌曲是否已在 playlist 中 —— 查询类 */
    @GetMapping("/api/playlist/isExistPlaylistSong")
    Integer isExistPlaylistSong(@RequestParam("songId") Integer songId);

    /** 新增 playlist-song 关联 —— 写操作，改 POST + RequestBody */
    @PostMapping("/api/playlist/addPlaylistSongRelation")
    void addPlaylistSongRelation(@RequestBody SongAdd_PVO vo);

    /** 批量删除关联 —— 写操作，改 POST + RequestBody */
    @PostMapping("/api/playlist/deleteSingerSongRelation")
    void deleteSingerSongRelation(@RequestBody List<Long> songIds);
}
