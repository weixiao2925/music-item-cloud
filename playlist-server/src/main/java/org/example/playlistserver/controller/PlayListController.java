package org.example.playlistserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.playlistserver.service.PlayListDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/playlist")
@RequiredArgsConstructor
public class PlayListController {

    private final PlayListDataService playListDataService;

    @ResponseBody
    @GetMapping("/getPlayListTableList")
    public RestBean<TableListVO> getPlayListTableList(@RequestParam int page,@RequestParam int pageSize) {
        String message=playListDataService.getPlayListTableListVerify(page,pageSize);
        return message!=null?
                RestBean.failure(400,message): RestBean.success(playListDataService.getPlayListTableList(page,pageSize));
    }
    @ResponseBody
    @GetMapping("/getPlayListDataOne")
    public RestBean<TableListVO> getPlayListDataOne(@RequestParam int playlist_id){
        return RestBean.success(playListDataService.getPlayDataList(playlist_id));
    }
    //搜索
    @ResponseBody
    @GetMapping("/searchPlayListTableList")
    public <T> RestBean<TableListVO> searchPlayListTableList(@RequestParam T searchText,@RequestParam int page,@RequestParam int pageSize) {
        String message=playListDataService.getPlayListTableListVerify(page,pageSize);
        return message !=null?
                RestBean.failure(400,message) : RestBean.success(playListDataService.getSearchDataTableList(searchText,page,pageSize));
    }
    //删除
    @ResponseBody
    @GetMapping("/deletePlayLists")
    public RestBean<Void> deletePlayList(@RequestParam List<Long> playListIds) {
        String message= playListDataService.deletePlayList(playListIds);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //修改
    @ResponseBody
    @GetMapping("/changePlayLists")
    public RestBean<Void> changePlayList(@RequestParam int playlist_id,@RequestParam String playlist_name,@RequestParam String category,@RequestParam String intro) {
        String message= playListDataService.updatePlayList(playlist_id,playlist_name,category,intro);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //提交歌单头像图片
    @ResponseBody
    @PostMapping("/upPlaylistAvatar")
    public RestBean<Void> upPlaylistAvatar(@RequestParam int playlist_id,@RequestParam MultipartFile file){
        String message=playListDataService.uploadFile(playlist_id,file);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //获取歌单图片
    @GetMapping("/getPlaylistAvatar")
    public ResponseEntity<org.springframework.core.io.Resource> getPlaylistAvatar(@RequestParam int playlist_id){
        return playListDataService.getFile(playlist_id);
    }

}
