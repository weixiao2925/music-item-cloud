package org.example.playlistserver.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.example.commoncore.constants.Const;
import org.example.commoncore.entity.dto.PlayListDataList;
import org.example.commoncore.entity.vo.request.SongAdd_PVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.example.playlistserver.mapper.PlayListDataMapper;
import org.example.playlistserver.service.PlayListDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.example.playlistserver.utils.GetAvatar.getResourceResponseEntity;

@Service
@RequiredArgsConstructor
public class PlayListDataServiceImpl extends ServiceImpl<PlayListDataMapper, PlayListDataList> implements PlayListDataService {

    private final PlayListDataMapper indexPlayListDataMapper;

    @Override
    public Integer getPlaylistSum() {
        return indexPlayListDataMapper.getPlaylistSum();
    }

    @Override
    public TableListVO getPlayListTableList(int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page =(page-1)*pageSize;
        PlayListDataList[] playListDataLists=indexPlayListDataMapper.getPlayListDataList(page,pageSize);
        int count= indexPlayListDataMapper.getPlayListSum();
        TableListVO vo=new TableListVO();
        vo.setCount(count);
        vo.setPlayListDataList(playListDataLists);
        return vo;
    }

    @Override
    public <T> TableListVO getSearchDataTableList(T searchText, int page, int pageSize) {
        if (page<1 || pageSize<1) {
            throw new IllegalArgumentException("请求参数（page,pageSize）不能为小于1的数");
        }
        page=(page-1)*pageSize;
        String keyName=String.valueOf(searchText);
        PlayListDataList[] playListDataLists=indexPlayListDataMapper.getPlayListDataByKeyname(keyName,page,pageSize);
        int totalCount= indexPlayListDataMapper.getSearchUPlayListCount(keyName);
        TableListVO vo=new TableListVO();
        vo.setCount(totalCount);
        vo.setPlayListDataList(playListDataLists);
        return vo;
    }

    @Override
    public String deletePlayList(List<Long> playListIds) {
        indexPlayListDataMapper.deletePlayList(playListIds);
        return null;
    }

    @Override
    public String updatePlayList(int playlist_id, String playlist_name, String category, String intro) {
//        System.out.println(playlist_id);
        PlayListDataList playListDataOne=indexPlayListDataMapper.playlistDataByPlaylistId(playlist_id);
        if (playListDataOne==null) return "暂无此歌单";
        if (playlist_name == null || playlist_name.isEmpty()) playlist_name=null;
        if (category == null || category.isEmpty()) category=null;
        if (intro == null || intro.isEmpty()) intro=null;
        indexPlayListDataMapper.updatePlayList(playlist_id,playlist_name,category,intro);
        return null;
    }
    //单个歌单信息
    @Override
    public TableListVO getPlayDataList(int playlist_id) {
        PlayListDataList playListDataOne=indexPlayListDataMapper.playlistDataByPlaylistId(playlist_id);
        TableListVO vo=new TableListVO();
        vo.setPlayListDataOne(playListDataOne);
        return vo;
    }

    //提交头像照片
    @Override
    public String uploadFile(int playlist_id, MultipartFile file) {
        // 获取原始文件名
        String fileName = file.getOriginalFilename();
//        System.out.println("原始文件名"+fileName);
        // 允许的文件后缀名集合
        List<String> allowedSuffixes = Arrays.asList(".jpg", ".jpeg", ".png", ".gif", ".bmp", ".webp");
        // 获取文件后缀
        String suffix = fileName.substring(fileName.lastIndexOf(".")).toLowerCase(); // 转换为小写以做不区分大小写的比较
//        System.out.println("后缀"+suffix);
        if (!allowedSuffixes.contains(suffix)) {
            return "请上传图片文件";
        }
        fileName= UUID.randomUUID()+suffix;
        //完整地址
        String fullFileName = Const.AVATAR_PATH+fileName;
        if (indexPlayListDataMapper.playlistDataByPlaylistId(playlist_id)==null) return "不存在该歌单";
        // 创建新文件名对象
        File dest = new File(fullFileName);
        //保存文件
        try {
            file.transferTo(dest);
            indexPlayListDataMapper.updatePlaylistPath(playlist_id,fullFileName);
            return null;
        } catch (IllegalStateException | IOException e) {
            e.printStackTrace(); // 可以打印错误日志帮助调试
            return "保存失败";
        }
    }

    //获取歌单图片
    @Override
    public ResponseEntity<org.springframework.core.io.Resource> getFile(int playlist_id) {
        return getResourceResponseEntity(indexPlayListDataMapper.getPlaylistPath(playlist_id));
    }

    @Override
    public Integer getSongsSumP(int playlist_id) {
        return indexPlayListDataMapper.getSongsSumP(playlist_id);
    }

    @Override
    public List<Integer> getSongIdList(int playlist_id, int page, int pageSize) {
        return indexPlayListDataMapper.getSongIdList(playlist_id,page,pageSize);
    }

    @Override
    public Integer isExistPlaylist(int playlist_id) {
        return indexPlayListDataMapper.isExistPlaylist(playlist_id);
    }

    @Override
    public List<Integer> getSongDataByKeyNameP(int playlist_id) {
        return indexPlayListDataMapper.getSongDataByKeyNameP(playlist_id);
    }

    @Override
    public Integer isExistPlaylistSong(Integer songId) {
        return indexPlayListDataMapper.isExistPlaylistSong(songId);
    }

    @Override
    @Transactional
    public void addPlaylistSongRelation(SongAdd_PVO vo) {
        indexPlayListDataMapper.addPlaylistSongRelation(vo);
    }

    @Override
    @Transactional
    public void deleteSingerSongRelation(List<Long> songIds) {
        indexPlayListDataMapper.deletePlaylistSongRelation(songIds);
    }


    //工具方法
    @Override
    public String getPlayListTableListVerify(int page, int pageSize) {
        return (page<1 || pageSize<1) ? "请求参数（page,pageSize）不能为小于1的数":null;
    }


}
