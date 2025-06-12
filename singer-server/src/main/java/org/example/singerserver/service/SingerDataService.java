package org.example.singerserver.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.apache.ibatis.annotations.Param;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SingerDataList;
import org.example.commoncore.entity.vo.request.SingerAddVO;
import org.example.commoncore.entity.vo.request.SongAddVO;
import org.example.commoncore.entity.vo.response.TableListVO;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
public interface SingerDataService extends IService<SingerDataList> {
    Integer getSingerSum();
    HomeDataList[] getSingerSexList();
    HomeDataList[] getSingerNationalityList();


    TableListVO getSingerTableList(int page, int pageSize);
    String getSingerTableListVerify(int page, int pageSize);
    <T> TableListVO getSearchDataTableList(T searchText, int page, int pageSize);

    String deleteSingerTables(List<Long> singerIds);
    String addSingerTable(SingerAddVO vo);
    String updateSingerTable(int singer_id,String singer_name,String sex,String nationality,String birthDateString,String intro);
    TableListVO getSingerDataList(int singer_id);
    String uploadFile(int singer_id,MultipartFile file);
    ResponseEntity<Resource> getFile(int singer_id);

    Integer getSongSum(int singer_id);
    List<Integer> getSongIdList(int singerId, int page,int pageSize);
    Integer isExist(Integer singer_id);
    void deleteSingers(List<Long> songIds);
    void addSSRelation(SongAddVO vo);
    List<Integer> getSongIdsBySingerId(int singerId);
}
