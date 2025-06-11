package org.example.songserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.Resource;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SongDataList;
import org.example.songserver.mapper.SongDataMapper;
import org.example.songserver.service.SongDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;


@Service
public class SongDataServiceImpl extends ServiceImpl<SongDataMapper, SongDataList> implements SongDataService {

    @Resource
    private SongDataMapper songDataMapper;


    @Override
    public Integer getSongSum () {
        return songDataMapper.getSongSum();
    }
    @Override
    public HomeDataList[] getSongTypeList() {
        return songDataMapper.getSongTypeList();
    }

    @Override
    public String updateSongData(int song_id, String title, String songType, String release_date) {
        SongDataList songDataList = songDataMapper.getSongData(song_id);
        if (songDataList == null) return "暂无此歌曲";
        if (title==null || title.isEmpty()) title=null;
        if (songType==null || songType.isEmpty()) songType=null;
        if (release_date==null || release_date.isEmpty()) release_date=null;
        if (release_date != null){
            try {
                System.out.println(release_date);
                Date.valueOf(release_date); // 将字符串转换为java.sql.Date
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "请求的日期格式有误";
            }
        }
        try {
            songDataMapper.updateSong(song_id,title,songType,release_date);
        }catch (Exception e){
            return "歌曲名不重复";
        }

        return null;
    }
    //提交歌曲头像图片
    @Override
    public String uploadFile(int song_id, MultipartFile file) {
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
//        System.out.println("原始文件名"+fileName);
        // 允许的文件后缀名集合
        List<String> allowedSuffixes = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");
        if (fileName == null) return "请上传文件";
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); // 转换为小写以做不区分大小写的比较
//        System.out.println("后缀"+suffix);
        if (!allowedSuffixes.contains(suffix)) {
            return "请上传图片文件";
        }
        fileName= UUID.randomUUID()+suffix;
        //完整地址
        String fullFileName = Const.AVATAR_PATH+fileName;
//        if (songDataMapper.isExist(song_id)==null) return "不存在该歌曲";
        // 创建新文件名对象
        File dest = new File(fullFileName);
        //保存文件
        try {
            file.transferTo(dest);
            songDataMapper.updateSongPath(song_id,fullFileName);
            return null;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace(); // 可以打印错误日志帮助调试
            return "保存失败";
        }
    }

    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getFile(int song_id) {
        return null;
    }

    //上传mp3文件
    public String uploadMp3File(int song_id, MultipartFile file) {
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
//        System.out.println("原始文件名"+fileName);
        // 允许的文件后缀名集合
        List<String> allowedSuffixes = List.of(".mp3");
        if (fileName == null) return "请上传文件";
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); // 转换为小写以做不区分大小写的比较
//        System.out.println("后缀"+suffix);
        if (!allowedSuffixes.contains(suffix)) {
            return "请上传mp3文件";
        }
        fileName= UUID.randomUUID()+suffix;
        //完整地址
        String fullFileName = Const.MP3_PATH+fileName;
//        if (songDataMapper.isExist(song_id)==null) return "不存在该歌曲";
        // 创建新文件名对象
        File dest = new File(fullFileName);
        //保存文件
        try {
            file.transferTo(dest);
            songDataMapper.updateAudioFilePath(song_id,fullFileName);
            return null;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace(); // 可以打印错误日志帮助调试
            return "保存失败";
        }
    }
    //上传歌词文件
    public String uploadLrcFile(int song_id, MultipartFile file) {
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
//        System.out.println("原始文件名"+fileName);
        // 允许的文件后缀名集合
        List<String> allowedSuffixes = List.of(".lrc");
        // 获取文件后缀
        String suffix; // 转换为小写以做不区分大小写的比较
        if (fileName == null) return "请上传文件";
        suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
//        System.out.println("后缀"+suffix);
        if (!allowedSuffixes.contains(suffix)) {
            return "请上传lrc文件";
        }
        fileName= UUID.randomUUID()+suffix;
        //完整地址
        String fullFileName = Const.LRC_PATH+fileName;
//        if (songDataMapper.isExist(song_id)==null) return "不存在该歌曲";
        // 创建新文件名对象
        File dest = new File(fullFileName);
        //保存文件
        try {
            file.transferTo(dest);
            songDataMapper.updateLyricsFilePath(song_id,fullFileName);
            return null;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace(); // 可以打印错误日志帮助调试
            return "保存失败";
        }
    }

}
