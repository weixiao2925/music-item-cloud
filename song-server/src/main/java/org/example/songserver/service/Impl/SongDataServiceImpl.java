package org.example.songserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import jakarta.annotation.Resource;
import lombok.RequiredArgsConstructor;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SongDataList;
import org.example.commoncore.entity.vo.request.SongAddVO;
import org.example.commoncore.entity.vo.request.SongAdd_PVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.songserver.feign.PlayListServerClient;
import org.example.songserver.feign.SingerServiceClient;
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
@RequiredArgsConstructor
public class SongDataServiceImpl extends ServiceImpl<SongDataMapper, SongDataList> implements SongDataService {

    private final SongDataMapper songDataMapper;
    private final SingerServiceClient singerServiceClient;
    private final PlayListServerClient playListServerClient;


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

    // 歌手
    @Override
    public TableListVO getSongTableList(int singer_id, int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page =(page-1)*pageSize;
        int count=singerServiceClient.getSongSum(singer_id);
        List<Integer> idList = singerServiceClient.getSongIdList(singer_id, page, pageSize);
        SongDataList[] songDataLists=songDataMapper.getSongDataList(idList);
        TableListVO vo=new TableListVO();
        vo.setCount(count);
        vo.setSongDataList(songDataLists);
        return vo;
    }

    @Override
    public String getSongTableListVerify(int singer_id, int page, int pageSize) {
        if (page<1 || pageSize<1)  return "请求参数（page,pageSize）不能为小于1的数";
        if (singerServiceClient.isExist(singer_id)!=1) return "请求歌手不存在";
        return null;
    }

    //搜索
    @Override
    public <T> TableListVO getSearchSongTableList(int singer_id, T searchText, int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page =(page-1)*pageSize;
        String keyName=String.valueOf(searchText);
        List<Integer> idList = singerServiceClient.getSongIdList(singer_id, page, pageSize);
        int count=songDataMapper.getSearchSongCount(idList,keyName);
        SongDataList[] songDataLists=songDataMapper.getSongDataByKeyName(idList,keyName,page,pageSize);
        TableListVO vo=new TableListVO();
        vo.setCount(count);
        vo.setSongDataList(songDataLists);
        return vo;
    }

    @Override
    public TableListVO getSongDataList(int singer_id, int song_id) {
        SongDataList songDataList=songDataMapper.getSongDataOne(song_id);
        TableListVO vo=new TableListVO();
        vo.setSongDataOne(songDataList);
        return vo;
    }

    //删除
    @Override
    public String deleteSongTableList(List<Long> songIds) {
        songDataMapper.deleteSongs(songIds);
        return null;
    }

    @Override
    public String addSong(SongAddVO vo) {
        if (vo==null) return "请输入正确参数";
        if (vo.getSinger_id()==0) return "请传入歌手id";
        if (singerServiceClient.isExist(vo.getSinger_id())!=1) return "请求歌手不存在";
        if (vo.getTitle()==null || vo.getTitle().isEmpty()) return "请传入歌曲名";
        if (vo.getRelease_date()==null || vo.getRelease_date().isEmpty()) vo.setRelease_date(null);
        if (vo.getRelease_date()!=null) {
            try {
                Date.valueOf(vo.getRelease_date());// 将字符串转换为java.sql.Date
            } catch (IllegalArgumentException ex) {
                System.out.println(ex.getMessage());
                return "请求的发行日期格式有误";
            }
        }
        try {
            songDataMapper.addSong(vo);
        }catch (Exception e){
            return "歌曲名不重复";
        }
        vo.setSong_id(songDataMapper.getSongId(vo.getTitle()));
        singerServiceClient.addSSRelation(vo);
        return null;
    }

    @Override
    public TableListVO getSongTableList_P(int playlist_id, int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page =(page-1)*pageSize;
        int count=playListServerClient.getSongsSumP(playlist_id);
        List<Integer> idList = playListServerClient.getSongIdList(playlist_id, page, pageSize);
        SongDataList[] songDataLists=songDataMapper.getSongDataListP(idList);
        TableListVO vo=new TableListVO();
        vo.setCount(count);
        vo.setSongDataList(songDataLists);
        return vo;
    }

    @Override
    public String getSongTableListVerify_P(int playlist_id, int page, int pageSize) {
        if (page<1 || pageSize<1)  return "请求参数（page,pageSize）不能为小于1的数";
        if (playListServerClient.isExistPlaylist(playlist_id)!=1) return "请求歌单不存在";
        return null;
    }

    @Override
    public <T> TableListVO getSearchSongTableList_P(int playlist_id, T searchText, int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page =(page-1)*pageSize;
        String keyName=String.valueOf(searchText);
        List<Integer> listId = playListServerClient.getSongDataByKeyNameP(playlist_id);
        int count=songDataMapper.getSearchSongCountP(listId,keyName);
        SongDataList[] songDataLists=songDataMapper.getSongDataByKeyNameP(listId,keyName,page,pageSize);
        TableListVO vo=new TableListVO();
        vo.setCount(count);
        vo.setSongDataList(songDataLists);
        return vo;
    }

    @Override
    public TableListVO getSongDataList_P(int playlist_id, int song_id) {
        SongDataList songDataList=songDataMapper.getSongDataOne_P(song_id);
        TableListVO vo=new TableListVO();
        vo.setSongDataOne(songDataList);
        return vo;
    }

    @Override
    public String addSong_P(SongAdd_PVO vo) {
        if (vo==null) return "请输入正确参数";
        if (vo.getPlaylist_id()==0) return "请传入歌单id";
        if (playListServerClient.isExistPlaylist(vo.getPlaylist_id())!=1) return "请求歌单不存在";
        if (vo.getTitle()==null || vo.getTitle().isEmpty()) return "请传入歌曲名";
        try {
            vo.setSong_id(songDataMapper.getSongId(vo.getTitle()));
        }catch (Exception e){
            return "歌曲不存在";
        }
        Integer songIdByTitle = songDataMapper.getSongIdByTitle(vo.getTitle());
        if (playListServerClient.isExistPlaylistSong(songIdByTitle)==1) return "请求歌曲已存在";
        playListServerClient.addPlaylistSongRelation(vo);
        return null;
    }
    //删除
    @Override
    public String deleteSongTableList_P(List<Long> songIds) {
        playListServerClient.deleteSingerSongRelation(songIds);
        return null;
    }

}
