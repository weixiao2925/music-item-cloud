package org.example.commoncore.entity.vo.response;

import lombok.Data;
import org.example.commoncore.entity.dto.PlayListDataList;
import org.example.commoncore.entity.dto.SingerDataList;
import org.example.commoncore.entity.dto.SongDataList;
import org.example.commoncore.entity.dto.UserDataList;

/**
 * 用于返回给前端的表格数据
 */
@Data
public class TableListVO {
    //返回有多少条数据
    Integer count;
    //返回用户管理的表格数组
    UserDataList[] userDataList;
    //返回单个用户的user数据
    UserDataList userDataOne;
    //返回单个歌手信息
    SingerDataList singerDataOne;
    //返回单个歌单信息
    PlayListDataList playListDataOne;
    //返回单个歌曲信息
    SongDataList songDataOne;
    //返回singer管理的表格数组
    SingerDataList[] singerDataList;
    //返回PlayList管理的表格数组
    PlayListDataList[] playListDataList;
    //返回song管理的表格数组
    SongDataList[] songDataList;
}
