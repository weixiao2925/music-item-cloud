package org.example.commoncore.entity.vo.response;


import lombok.AllArgsConstructor;
import lombok.Data;
import org.example.commoncore.entity.dto.HomeDataList;

//用于返回给主页信息
@Data
@AllArgsConstructor
public class HoneDataSumVO {
    int userSum;
    int songSum;
    int singerSum;
    int playlistSum;
    HomeDataList[] userSexList;
    HomeDataList[] songTypeList;
    HomeDataList[] singerSexList;
    HomeDataList[] singerNationList;
}
