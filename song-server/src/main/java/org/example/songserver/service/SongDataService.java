package org.example.songserver.service;

import com.baomidou.mybatisplus.extension.service.IService;

import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SongDataList;
import org.example.commoncore.entity.vo.request.SongAddVO;
import org.example.commoncore.entity.vo.request.SongAdd_PVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface SongDataService extends IService<SongDataList> {
    Integer getSongSum() ;
    HomeDataList[] getSongTypeList() ;


    String updateSongData(int song_id, String title, String songType, String release_date);
    String uploadFile(int song_id, MultipartFile file);
    ResponseEntity<Resource> getFile(int song_id);
    String uploadMp3File(int song_id, MultipartFile file);
    String uploadLrcFile(int song_id, MultipartFile file);
//
//    String deleteSongTableListP(List<Long> songIds);
//    String addSongP(SongAddVO vo);

    TableListVO getSongTableList(int singer_id, int page, int pageSize);
    String getSongTableListVerify(int singer_id,int page,int pageSize);
    <T> TableListVO getSearchSongTableList(int singer_id,T searchText,int page,int pageSize);
    TableListVO getSongDataList(int singer_id,int song_id);

    String deleteSongTableList(List<Long> songIds);
    String addSong(SongAddVO vo);

    //歌单
    TableListVO getSongTableList_P(int playlist_id, int page, int pageSize);
    String getSongTableListVerify_P(int playlist_id, int page, int pageSize);
    <T> TableListVO getSearchSongTableList_P(int playlist_id, T searchText, int page, int pageSize);
    TableListVO getSongDataList_P(int playlist_id, int song_id);
    String addSong_P(SongAdd_PVO vo);
    String deleteSongTableList_P(List<Long> songIds);
}
