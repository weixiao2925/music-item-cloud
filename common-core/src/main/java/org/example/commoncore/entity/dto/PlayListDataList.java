package org.example.commoncore.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 用于PlayList管路页的表格数据
 */
@Data
@AllArgsConstructor
public class PlayListDataList {
    int playlist_id;
    String playlist_name;
    String user_name;
    String category;
    String intro;
    String playList_path;
}
