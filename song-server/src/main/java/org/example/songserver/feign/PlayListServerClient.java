package org.example.songserver.feign;

import org.example.commoncore.entity.vo.request.SongAdd_PVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@FeignClient(name = "backstage-playlist-server", url = "https://localhost:9003")
public interface PlayListServerClient {


    @GetMapping("/api/playlist/getSongsSumP")
    Integer getSongsSumP(int playlist_id);
    @GetMapping("/api/playlist/getSongIdList")
    List<Integer> getSongIdList(int playlist_id, int page, int pageSize);
    @GetMapping("/api/playlist/isExistPlaylist")
    Integer isExistPlaylist(int playlist_id);
    @GetMapping("/api/playlist/getSongDataByKeyNameP")
    List<Integer> getSongDataByKeyNameP(int playlist_id);
    @GetMapping("/api/playlist/isExistPlaylistSong")
    Integer isExistPlaylistSong(Integer songId);
    @GetMapping("/api/playlist/addPlaylistSongRelation")
    void addPlaylistSongRelation(SongAdd_PVO vo);
    @GetMapping("/api/playlist/deleteSingerSongRelation")
    void deleteSingerSongRelation(List<Long> songIds);


}
