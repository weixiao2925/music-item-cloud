package org.example.songserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SongDataList;
import org.example.commoncore.entity.vo.request.SongAddVO;

import java.util.List;

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


    // 联合 singer
    //查询对应singer的song分页(根据歌手传过来的歌曲id查出对应的歌曲)
    @Select({
            "<script>",
            "SELECT * FROM songs WHERE song_id IN",
            "<foreach collection='list' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    SongDataList[] getSongDataList(@Param("list") List<Integer> ids);
    //获取单个歌手信息
    @Select("""
        SELECT * FROM songs WHERE song_id = #{song_id}
    """)
    SongDataList getSongDataOne(@Param("song_id") int song_id);
    //----搜索
    @Select({
            "<script>",
            "SELECT * FROM songs",
            "WHERE song_id IN",
            "<foreach collection='songIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "AND title LIKE CONCAT('%', #{keyName}, '%')",
            "LIMIT #{page}, #{pageSize}",
            "</script>"
    })
    SongDataList[] getSongDataByKeyName(@Param("songIds") List<Integer> songIds,
                                          @Param("keyName") String keyName,
                                          @Param("page") int page,
                                          @Param("pageSize") int pageSize);
    //搜索出来的数据总数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM songs",
            "WHERE song_id IN",
            "<foreach collection='songIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "AND title LIKE CONCAT('%', #{keyName}, '%')",
            "</script>"
    })
    int getSearchSongCount(@Param("songIds") List<Integer> songIds,
                          @Param("keyName") String keyName);
    //----添加数据(先添加到数据库，然后再从数据库根据title获取id传入addSSRelation)
    @Insert("insert into songs( title, songType, release_date) values (#{title},#{songType},#{release_date})")
    void addSong(SongAddVO vo);


    // 联合playlist
    //查询对应的playlist的song分页(查出歌单对应的所有歌曲id)
    @Select({
            "<script>",
            "SELECT * FROM songs WHERE song_id IN",
            "<foreach collection='list' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "</script>"
    })
    List<SongDataList> getSongDataListP(@Param("list") List<Integer> ids);
    //获取单个歌单信息
    @Select("""
        SELECT * FROM songs WHERE song_id = #{song_id}
    """)
    SongDataList getSongDataOne_P(@Param("song_id") int song_id);
    //----搜索
    @Select({
            "<script>",
            "SELECT * FROM songs",
            "WHERE song_id IN",
            "<foreach collection='songIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "AND title LIKE CONCAT('%', #{keyName}, '%')",
            "LIMIT #{page}, #{pageSize}",
            "</script>"
    })
    List<SongDataList> getSongDataByKeyNameP(@Param("songIds") List<Integer> songIds,
                                            @Param("keyName") String keyName,
                                            @Param("page") int page,
                                            @Param("pageSize") int pageSize);
    //搜索出来的数据总数
    @Select({
            "<script>",
            "SELECT COUNT(*) FROM songs",
            "WHERE song_id IN",
            "<foreach collection='songIds' item='id' open='(' separator=',' close=')'>",
            "#{id}",
            "</foreach>",
            "AND title LIKE CONCAT('%', #{keyName}, '%')",
            "</script>"
    })
    int getSearchSongCountP(@Param("songIds") List<Integer> songIds,
                          @Param("keyName") String keyName);
    //----添加歌曲歌单版
    //先看是否存在(先根据title查出歌曲名，后面去关系吧查看有没有id)
    @Select("SELECT song_id FROM songs WHERE title = #{title}")
    Integer getSongIdByTitle(@Param("title") String title);


    //----删除数据
    //直接批量删除
    @Delete("<script>" +
            "delete from songs where song_id in " +
            "<foreach item='songId' collection='songIds' open='(' separator=',' close=')'>" +
            "#{songId}" +
            "</foreach>" +
            "</script>")
    void deleteSongs(@Param("songIds") List<Long> songIds);


}
