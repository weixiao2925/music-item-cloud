package org.example.playlistserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.commoncore.entity.dto.PlayListDataList;
import org.example.commoncore.entity.vo.request.SongAdd_PVO;

import java.util.List;

@Mapper
public interface PlayListDataMapper extends BaseMapper<PlayListDataList> {
    //获取playList总数
    @Select("select count(*) from playlist")
    Integer getPlaylistSum();


    //分页查询
    @Select("select playlist_id, playlist_name, user_name, category, intro, playList_path from playlist limit #{page},#{pageSize}")
    PlayListDataList[] getPlayListDataList(@Param("page") int page,@Param("pageSize") int pageSize);
    //查询Playlist总数
    @Select("select count(*) from playlist")
    Integer getPlayListSum();
    //搜索
    @Select("select playlist_id, playlist_name, user_name, category, intro, playList_path from playlist where playlist_name like concat('%' , #{keyname}, '%') or user_name like concat('%', #{keyname}, '%') limit #{page},#{pageSize}")
    PlayListDataList[] getPlayListDataByKeyname(@Param("keyname") String keyname,@Param("page") int page,@Param("pageSize") int pageSize);
    //搜索出来的数据总数
    @Select("select count(*) from playlist where playlist_name like concat('%' , #{keyname}, '%') or user_name like concat('%', #{keyname}, '%')")
    Integer getSearchUPlayListCount(@Param("keyname") String keyname);
    //根据id查找playlist信息
    @Select("select * from playlist where playlist_id=#{playlist_id}")
    PlayListDataList playlistDataByPlaylistId(@Param("playlist_id") int playlist_id);
    //----删除数据
    //直接批量删除
    @Delete("<script>" +
            "delete from playlist where playlist_id in " +
            "<foreach item='playListId' collection='playListIds' open='(' separator=',' close=')'>" +
            "#{playListId}" +
            "</foreach>" +
            "</script>")
    void deletePlayList(@Param("playListIds") List<Long> playListIds);
    //--修改
    @Update("update playlist set playlist_name=#{playlist_name},category=#{category},intro=#{intro} where playlist_id=#{playlist_id}")
    void updatePlayList(@Param("playlist_id") int playlist_id,@Param("playlist_name") String playlist_name,@Param("category") String category,@Param("intro") String intro);

    //----上传头像
    @Update("update playlist set playList_path=#{playList_path} where playlist_id=#{playlist_id}")
    void updatePlaylistPath(@Param("playlist_id") int playlist_id,@Param("playList_path") String playList_path);
    //----获取头像地址
    @Select("select playList_path from playlist where playlist_id=#{playlist_id}")
    String getPlaylistPath(@Param("playlist_id") int playlist_id);


    // 联合playlist
    //查询是否存在playlist这个id
    @Select("select exists(select 1 from playlist where playlist_id=#{playlist_id})")
    Integer isExistPlaylist(@Param("playlist_id") Integer playlist_id);
    //查询对应的playlist的song分页
    @Select("select song_id from playlist_song_relation where playlist_id = #{playlist_id} limit #{page}, #{pageSize}")
    List<Integer> getSongIdList(@Param("playlist_id") int playlist_id, @Param("page") int page, @Param("pageSize") int pageSize);
    //查询对应的playlist的song总数
    @Select("select count(*) from playlist_song_relation where playlist_id = #{playlist_id}")
    Integer getSongsSumP(@Param("playlist_id") int playlist_id);
    //----搜索 + //搜索出来的数据总数
    @Select("select song_id from playlist_song_relation where playlist_id = #{playlist_id}")
    List<Integer> getSongDataByKeyNameP(@Param("playlist_id") int playlist_id);
    //----添加歌曲歌单版
    //先看是否存在
    @Select("SELECT EXISTS(SELECT 1 FROM playlist_song_relation WHERE song_id = #{songId})")
    Integer isExistPlaylistSong(@Param("songId") Integer songId);
    @Insert("insert into playlist_song_relation(playlist_id, song_id) VALUES (#{playlist_id},#{song_id})")
    void addPlaylistSongRelation(SongAdd_PVO vo);



    //歌单版删除（只删除歌曲与歌单的关系）
    @Delete("<script>" +
            "delete from playlist_song_relation where song_id in " +
            "<foreach item='songId' collection='songIds' open='(' separator=',' close=')'>" +
            "#{songId}" +
            "</foreach>" +
            "</script>")
    void deletePlaylistSongRelation(@Param("songIds") List<Long> songIds);



}
