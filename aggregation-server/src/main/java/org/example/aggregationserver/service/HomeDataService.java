package org.example.aggregationserver.service;

import org.example.commoncore.entity.vo.response.HoneDataSumVO;
import org.springframework.stereotype.Service;

@Service
public interface HomeDataService {
     HoneDataSumVO getHomeDataList();
}
