package org.example.playlistserver.service;

import org.example.commoncore.entity.vo.response.TableListVO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PlayListDataService {
    Integer getPlaylistSum();

    TableListVO getPlayListTableList(int page, int pageSize);
    String getPlayListTableListVerify(int page, int pageSize);
    <T> TableListVO getSearchDataTableList(T searchText, int page, int pageSize);

    String deletePlayList(List<Long> playListIds);
    String updatePlayList(int playlist_id, String playlist_name,String category,String intro);
    TableListVO getPlayDataList(int playlist_id);
    String uploadFile(int playlist_id, MultipartFile file);
    ResponseEntity<Resource> getFile(int playlist_id);
}
