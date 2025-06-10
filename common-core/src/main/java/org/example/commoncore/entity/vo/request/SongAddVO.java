package org.example.commoncore.entity.vo.request;

import lombok.Data;

@Data
public class SongAddVO {
    String title;
    String songType="";
    String release_date=null;
    int singer_id;
    int song_id;
}
