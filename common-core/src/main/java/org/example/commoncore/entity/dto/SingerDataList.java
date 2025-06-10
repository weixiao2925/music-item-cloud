package org.example.commoncore.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

/**
 * 用于Singer管理页的表格数据
 */
@Data
@AllArgsConstructor
public class SingerDataList {
    int singer_id;
    String singer_name;
    String sex;
    String nationality;
    Date birth_day;
    String intro;
    String singer_path;
}
