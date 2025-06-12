package org.example.singerserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.commoncore.entity.RestBean;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.vo.request.SingerAddVO;
import org.example.commoncore.entity.vo.request.SongAddVO;
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

    @GetMapping("/getSingerSum")
    public Integer getSingerSum() {
        return singerDataService.getSingerSum();
    }
    @GetMapping("/getSingerSexList")
    public HomeDataList[] getSingerSexList() {
        return singerDataService.getSingerSexList();
    }
    @GetMapping("/getSingerNationalityList")
    public HomeDataList[] getSingerNationalityList() {
        return singerDataService.getSingerNationalityList();
    }

    // 歌曲
    @GetMapping("/getSongSum")
    public Integer getSongSum(@RequestParam("singer_id") int singer_id) {
        return singerDataService.getSongSum(singer_id);
    }
    @GetMapping("/getSongIdList")
    public List<Integer> getSongIdList(@RequestParam("singer_id") int singer_id,
                                       @RequestParam("page") int page,
                                       @RequestParam("pageSize") int pageSize) {
        return singerDataService.getSongIdList(singer_id, page, pageSize);
    }
    @GetMapping("/isExist")
    public Integer isExist(@RequestParam("singer_id") Integer singer_id) {
        return singerDataService.isExist(singer_id);
    }
    @GetMapping("/deleteSingersSongRelation")
    public void  deleteSingersSongRelation(@RequestParam("songIds") List<Long> songIds) {
        singerDataService.deleteSingers(songIds);
    }
    @GetMapping("/addSSRelation")
    public void addSSRelation(@RequestBody SongAddVO vo) {
        singerDataService.addSSRelation(vo);
    }


    @ResponseBody
    @GetMapping("/getSingerTableList")
    public RestBean<TableListVO> getSingerTableList(@RequestParam("page") int page, @RequestParam("pageSize") int pageSize) {
        String message=singerDataService.getSingerTableListVerify(page,pageSize);
        return message!=null?
                RestBean.failure(400,message): RestBean.success(singerDataService.getSingerTableList(page,pageSize));
    }
    //搜索
    @ResponseBody
    @GetMapping("/searchSingerTableList")
    public <T> RestBean<TableListVO> searchSingerTableList(@RequestParam("searchText") T searchText,@RequestParam("page") int page,@RequestParam("pageSize") int pageSize) {
        String message=singerDataService.getSingerTableListVerify(page,pageSize);
        return message!=null?
                RestBean.failure(400,message) : RestBean.success(singerDataService.getSearchDataTableList(searchText,page,pageSize));
    }
    @ResponseBody
    @GetMapping("/getSingerOneData")
    public RestBean<TableListVO> getSingerOneData(@RequestParam("singer_id") int singer_id){
        return RestBean.success(singerDataService.getSingerDataList(singer_id));
    }
    //删除
    @ResponseBody
    @GetMapping("/deleteSingers")
    public RestBean<Void> deleteSinger(@RequestParam("singerIds") List<Long> singerIds) {
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
    public RestBean<Void> changeSinger(@RequestParam("singer_id") int singer_id,
                                       @RequestParam("singer_name") String singer_name,
                                       @RequestParam("sex") String sex,
                                       @RequestParam("nationality") String nationality,
                                       @RequestParam("birthDateString") String birthDateString,
                                       @RequestParam("intro") String intro){
        String message=singerDataService.updateSingerTable(singer_id,singer_name,sex,nationality,birthDateString,intro);
        return message != null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //提交歌手头像图片
    @ResponseBody
    @PostMapping("/upSingerAvatar")
    public RestBean<Void> upSingerAvatar(@RequestParam("singer_id") int singer_id,@RequestParam("file") MultipartFile file){
        String message=singerDataService.uploadFile(singer_id,file);
        return message !=null ?
                RestBean.failure(400,message) : RestBean.success();
    }
    //获取歌手图片
    @GetMapping("/getSingerAvatar")
    public ResponseEntity<Resource> getSingerAvatar(@RequestParam("singer_id") int singer_id){
//        System.out.println("获取歌手头像，singer_id: " + singer_id);
        //        System.out.println(file.getBody());
        return singerDataService.getFile(singer_id);
    }

}
