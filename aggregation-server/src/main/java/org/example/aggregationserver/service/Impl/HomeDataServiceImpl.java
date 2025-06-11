package org.example.aggregationserver.service.Impl;

import lombok.RequiredArgsConstructor;
import org.example.aggregationserver.feign.PlayListServerClientFeign;
import org.example.aggregationserver.feign.SingerServerClientFeign;
import org.example.aggregationserver.feign.SongServerClientFeign;
import org.example.aggregationserver.feign.UserServerClientFeign;
import org.example.aggregationserver.service.HomeDataService;
import org.example.commoncore.entity.vo.response.HoneDataSumVO;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HomeDataServiceImpl implements HomeDataService {

    private final PlayListServerClientFeign playListServerClientFeign;
    private final SingerServerClientFeign singerServerClientFeign;
    private final SongServerClientFeign songServerClientFeign;
    private final UserServerClientFeign userServerClientFeign;

    //只要验证token就可以给
    public HoneDataSumVO getHomeDataList(){
        return new HoneDataSumVO(
                userServerClientFeign.getUserSum(),
                songServerClientFeign.getSongSum(),
                singerServerClientFeign.getSingerSum(),
                playListServerClientFeign.getPlaylistSum(),
                userServerClientFeign.getUserSexList(),
                songServerClientFeign.getSongTypeList(),
                singerServerClientFeign.getSingerSexList(),
                singerServerClientFeign.getSingerNationalityList()
        );

    }
}
