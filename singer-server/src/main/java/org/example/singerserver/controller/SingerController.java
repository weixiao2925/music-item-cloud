package org.example.singerserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.vo.request.SingerAddVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.singerserver.service.SingerDataService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/singer")
@RequiredArgsConstructor
public class SingerController {

    private final SingerDataService singerDataService;

    @ResponseBody
    @GetMapping("/getSingerTableList")
    public RestBean<TableListVO> getSingerTableList(@RequestParam int page, @RequestParam int pageSize) {
        String message=singerDataService.getSingerTableListVerify(page,pageSize);
        return message!=null?
                RestBean.failure(400,message): RestBean.success(singerDataService.getSingerTableList(page,pageSize));
    }
    //搜索
    @ResponseBody
    @GetMapping("/searchSingerTableList")
    public <T> RestBean<TableListVO> searchSingerTableList(@RequestParam T searchText,@RequestParam int page,@RequestParam int pageSize) {
        String message=singerDataService.getSingerTableListVerify(page,pageSize);
        return message!=null?
                RestBean.failure(400,message) : RestBean.success(singerDataService.getSearchDataTableList(searchText,page,pageSize));
    }
    @ResponseBody
    @GetMapping("/getSingerOneData")
    public RestBean<TableListVO> getSingerOneData(@RequestParam int singer_id){
        return RestBean.success(singerDataService.getSingerDataList(singer_id));
    }
    //删除
    @ResponseBody
    @GetMapping("/deleteSingers")
    public RestBean<Void> deleteSinger(@RequestParam List<Long> singerIds) {
        String message=singerDataService.deleteSingerTables(singerIds);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //添加
    @ResponseBody
    @PostMapping("/addSinger")
    public RestBean<Void> addSinger(@RequestBody SingerAddVO vo){
        String message=singerDataService.addSingerTable(vo);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //修改
    @ResponseBody
    @GetMapping("/changeSinger")
    public RestBean<Void> changeSinger(@RequestParam int singer_id,
                                       @RequestParam String singer_name,
                                       @RequestParam String sex,
                                       @RequestParam String nationality,
                                       @RequestParam String birthDateString,
                                       @RequestParam String intro){
        String message=singerDataService.updateSingerTable(singer_id,singer_name,sex,nationality,birthDateString,intro);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //提交歌手头像图片
    @ResponseBody
    @PostMapping("/upSingerAvatar")
    public RestBean<Void> upSingerAvatar(@RequestParam int singer_id,@RequestParam MultipartFile file){
        String message=singerDataService.uploadFile(singer_id,file);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //获取歌手图片
    @GetMapping("/getSingerAvatar")
    public ResponseEntity<Resource> getSingerAvatar(@RequestParam int singer_id){
        return singerDataService.getFile(singer_id);
    }

}
