package org.example.songserver.service;

import com.baomidou.mybatisplus.extension.service.IService;

import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SongDataList;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

}
