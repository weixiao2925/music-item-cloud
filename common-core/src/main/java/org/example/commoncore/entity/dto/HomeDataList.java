package org.example.commoncore.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

//用于首页的可视化数据
@Data
@AllArgsConstructor
public class HomeDataList {
    String value;
    int sum;
}
