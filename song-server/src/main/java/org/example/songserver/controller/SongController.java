package org.example.songserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.vo.request.SongAddVO;
import org.example.commoncore.entity.vo.request.SongAdd_PVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.songserver.service.SongDataService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/song")
@RequiredArgsConstructor
public class SongController {

    private final SongDataService songDataService;

    @GetMapping("/getSongSum")
    public Integer getSongSum() {
        return songDataService.getSongSum();
    }
    @GetMapping("/getSongTypeList")
    public HomeDataList[] getSongTypeList() {
        return songDataService.getSongTypeList();
    }

    //----songs页数据
    //--singer
    @ResponseBody
    @GetMapping("/getSongTableList")
    public RestBean<TableListVO> getSongTableList(@RequestParam int singer_id, @RequestParam int page, @RequestParam int pageSize) {
        String message= songDataService.getSongTableListVerify(singer_id,page,pageSize);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success(songDataService.getSongTableList(singer_id,page,pageSize));
    }
    @ResponseBody
    @GetMapping("/getSongDataOne")
    public RestBean<TableListVO> getSongDataOne(@RequestParam int singer_id,@RequestParam int song_id) {
        return RestBean.success(songDataService.getSongDataList(singer_id,song_id));
    }
    //搜索
    @ResponseBody
    @GetMapping("/searchSongTableList")
    public <T> RestBean<TableListVO> getSongTableList(@RequestParam int singer_id,@RequestParam T searchText,@RequestParam int page,@RequestParam int pageSize) {
        String message=songDataService.getSongTableListVerify(singer_id,page,pageSize);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success(songDataService.getSearchSongTableList(singer_id,searchText,page,pageSize));
    }
    //添加
    @ResponseBody
    @PostMapping("/addSong")
    public RestBean<Void> addSong(@RequestBody SongAddVO vo) {
        String message= songDataService.addSong(vo);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //删除
    @ResponseBody
    @GetMapping("/deleteSongs")
    public RestBean<Void> deleteSongs(@RequestParam List<Long> songIds) {
        String message=songDataService.deleteSongTableList(songIds);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //--playlist
    @ResponseBody
    @GetMapping("/getSongTableList_P")
    public RestBean<TableListVO> getSongTableList_P(@RequestParam int playlist_id,@RequestParam int page,@RequestParam int pageSize) {
        String message= songDataService.getSongTableListVerify_P(playlist_id,page,pageSize);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success(songDataService.getSongTableList_P(playlist_id,page,pageSize));
    }
    @ResponseBody
    @GetMapping("/getSongDataOne_P")
    public RestBean<TableListVO> getSongDataOne_P(@RequestParam int playlist_id,@RequestParam int song_id) {
        return RestBean.success(songDataService.getSongDataList_P(playlist_id,song_id));
    }
    //搜索
    @ResponseBody
    @GetMapping("/searchSongTableList_P")
    public <T> RestBean<TableListVO> getSongTableList_P(@RequestParam int playlist_id,@RequestParam T searchText,@RequestParam int page,@RequestParam int pageSize) {
        String message=songDataService.getSongTableListVerify_P(playlist_id,page,pageSize);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success(songDataService.getSearchSongTableList_P(playlist_id,searchText,page,pageSize));
    }
    //添加
    @ResponseBody
    @PostMapping("/addSong_P")
    public RestBean<Void> addSong_P(@RequestBody SongAdd_PVO vo) {
        String message= songDataService.addSong_P(vo);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //删除
    @ResponseBody
    @GetMapping("/deleteSongs_P")
    public RestBean<Void> deleteSongs_P(@RequestParam List<Long> songIds) {
        String message=songDataService.deleteSongTableList_P(songIds);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //--公共
    //修改
    @ResponseBody
    @GetMapping("/changeSongs")
    public RestBean<Void> changeSongs(@RequestParam int song_id,@RequestParam  String title,@RequestParam  String songType,@RequestParam  String release_date) {
        String message=songDataService.updateSongData(song_id,title,songType,release_date);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //提交歌曲头像图片
    @ResponseBody
    @PostMapping("/upSongAvatar")
    public RestBean<Void> upSongAvatar(@RequestParam int song_id,@RequestParam MultipartFile file){
        String message=songDataService.uploadFile(song_id,file);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //获取歌曲图片
    @GetMapping("/getSongAvatar")
    public ResponseEntity<Resource> upSongAvatar(@RequestParam int song_id){
        return songDataService.getFile(song_id);
    }
    //提交歌曲MP3文件
    @ResponseBody
    @PostMapping("/upSongMp3")
    public RestBean<Void> upSongMp3(@RequestParam int song_id,@RequestParam MultipartFile file){
        String message=songDataService.uploadMp3File(song_id,file);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //提交歌词lrc文件
    @ResponseBody
    @PostMapping("/upSongLrc")
    public RestBean<Void> upSongLrc(@RequestParam int song_id,@RequestParam MultipartFile file){
        String message=songDataService.uploadLrcFile(song_id,file);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }

}
