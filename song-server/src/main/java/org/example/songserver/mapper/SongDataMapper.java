package org.example.songserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SongDataList;

@Mapper
public interface SongDataMapper extends BaseMapper<SongDataList> {
    //获取Song总数
    @Select("select count(*) from songs")
    Integer getSongSum();
    //查询song类型
    @Select("select songs.songType as value, count(*) as sum from songs group by songs.songType")
    HomeDataList[] getSongTypeList();



    //根据歌曲名查找歌曲id
    @Select("select song_id from songs where title=#{title}")
    int getSongId(@Param("title") String title);
    @Select("select * from songs where song_id=#{song_id}")
    SongDataList getSongData(@Param("song_id") Integer song_id);
    //----修改
    @Update("update songs set title=#{title},songType=#{songType},release_date=#{release_date} where song_id=#{song_id}")
    void updateSong(@Param("song_id") int song_id,@Param("title") String title,@Param("songType") String songType,@Param("release_date") String release_date);
    //    int song_id, String title, String songType, String release_date
    //----上传头像
    @Update("update songs set song_path=#{song_path} where song_id=#{song_id}")
    void updateSongPath(int song_id, String song_path);
    //----获取头像地址
    @Select("select song_path from songs where song_id=#{song_id}")
    String getSongPath(int song_id);
    //----上传mp3文件
    @Update("update songs set audio_file_path=#{audio_file_path} where song_id=#{song_id}")
    void updateAudioFilePath(int song_id, String audio_file_path);
    //----上传lrc文件
    @Update("update songs set lyrics_file_path=#{lyrics_file_path} where song_id=#{song_id}")
    void updateLyricsFilePath(int song_id, String lyrics_file_path);

}
