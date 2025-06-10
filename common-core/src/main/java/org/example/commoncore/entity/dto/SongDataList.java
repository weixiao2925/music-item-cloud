package org.example.commoncore.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Date;

@Data
@AllArgsConstructor
public class SongDataList {
    int song_id;
    String title;
    String songType;
    String audio_file_path;
    String lyrics_file_path;
    Date release_date;
    String song_path;
}
