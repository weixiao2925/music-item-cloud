package org.example.singerserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.commoncore.entity.dto.HomeDataList;
import org.example.commoncore.entity.dto.SingerDataList;
import org.example.commoncore.entity.vo.request.SingerAddVO;
import org.example.commoncore.entity.vo.request.SongAddVO;

import java.sql.Date;
import java.util.List;

@Mapper
public interface SingerDataMapper extends BaseMapper<SingerDataList> {
    //获取singer总数
    @Select("select count(*) from singer")
    Integer getSingerSum();
    //查询singer性别
    @Select("select singer.sex as value,count(*) as sum from singer group by singer.sex")
    HomeDataList[] getSingerSexList();
    //查询singer国籍
    @Select("select singer.nationality as value, count(*) as sum from singer group by singer.nationality")
    HomeDataList[] getSingerNationalityList();




    //----查询
    //分页查询
    @Select("select singer_id, singer_name, singer.sex, singer.nationality, singer.birth_date, singer.intro, singer.singer_path from singer limit #{page},#{pageSize}")
    SingerDataList[] getSingerDataList(@Param("page") int page,@Param("pageSize") int pageSize);

    //根据singer_id查找
    @Select("select * from singer where singer_id=#{singer_id}")
    SingerDataList getSingerDataBySingerId(int singer_id);

    //----搜索
    @Select("select singer_id, singer_name, singer.sex, singer.nationality, singer.birth_date, singer.intro, singer.singer_path from singer where singer_name like concat('%', #{keyName}, '%') limit #{page},#{pageSize}")
    SingerDataList[] getSingerDataByKeyName(String keyName,@Param("page") int page,@Param("pageSize") int pageSize);
    //搜索出来的数据总数
    @Select("select count(*) from singer where singer_name like concat('%',#{keyName},'%')")
    int getSearchSingerCount(String keyName);

    //----删除数据
    //直接批量删除
    @Delete("<script>" +
            "delete from singer where singer_id in " +
            "<foreach item='singerId' collection='singerIds' open='(' separator=',' close=')'>" +
            "#{singerId}" +
            "</foreach>" +
            "</script>")
    void deleteSingers(@Param("singerIds") List<Long> singerIds);


    //增加数据
    @Insert("insert into singer (singer_name, sex, nationality, birth_date, intro) values(#{singer_name},#{sex},#{nationality},#{birth_date},#{intro}) ")
    void insertSinger(SingerAddVO vo);

    //----修改数据
    @Update("update singer set singer_name=#{singer_name},sex=#{sex},nationality=#{nationality},birth_date=#{birth_date},intro=#{intro} where singer_id=#{singer_id}")
    void updateSinger(@Param("singer_id") int singer_id, @Param("singer_name") String singer_name, @Param("sex") String sex, @Param("nationality") String nationality, @Param("birth_date") Date birth_date, @Param("intro") String intro);


    //----上传头像
    @Update("update singer set singer_path=#{singer_path} where singer_id=#{singer_id}")
    void updateSingerPath(int singer_id,String singer_path);
    //----获取头像地址
    @Select("select singer_path from singer where singer_id=#{singer_id}")
    String getSingerPath(int singer_id);


    // 联合 song
    //查询是否存在singer这个id
    @Select("select exists(select 1 from singer where singer_id=#{singer_id})")
    Integer isExist(@Param("singer_id") Integer singer_id);
    //查询对应的playlist的song分页(查出歌单对应的所有歌曲id)
    @Select("select song_id from singer_song_relation where singer_id = #{singer_id} limit #{page}, #{pageSize}")
    List<Integer> getSongIdList(@Param("singer_id") int singerId, @Param("page") int page, @Param("pageSize") int pageSize);
    //查询对应singer的song总数
    @Select("select count(*) from singer_song_relation where singer_id = #{singer_id}")
    Integer getSongsSum(@Param("singer_id") int singer_id);
    //----搜索 + //搜索出来的数据总数
    @Select("select song_id from singer_song_relation where singer_id = #{singerId}")
    List<Integer> getSongIdsBySingerId(@Param("singerId") int singerId);
    //----添加数据(先添加到数据库，然后再从数据库根据title获取id传入addSSRelation)
    @Insert("insert into singer_song_relation (singer_id, song_id) values (#{singer_id},#{song_id})")
    void addSSRelation(@Param("singer_id") int singer_id, @Param("song_id") int song_id);

    //歌单版删除（只删除歌曲与歌单的关系）
    @Delete("<script>" +
            "delete from singer_song_relation where song_id in " +
            "<foreach item='songId' collection='songIds' open='(' separator=',' close=')'>" +
            "#{songId}" +
            "</foreach>" +
            "</script>")
    void deleteSingerSongRelation(@Param("songIds") List<Long> songIds);

}
