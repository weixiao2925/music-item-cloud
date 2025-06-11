package org.example.userserver.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.*;
import org.example.commoncore.entity.dto.UserDataList;
import org.example.commoncore.entity.vo.response.AccountInfoVO;

import java.sql.Date;
import java.util.List;

@Mapper
public interface IndexUserDataMapper extends BaseMapper<UserDataList> {

    @Select("""
        select user_id, username, name, sex, birth_date, region, signature, avatar_path, role, registration_time from `mc-user`.users
        where username = #{username}
    """)
    AccountInfoVO getUserAccountInfo(@Param("username") String username);

    //----用户管理页
    //--查询
    //分页查询
    @Select("""
            select user_id as id,name, sex, username as email, birth_date, signature, region, avatar_path ,registration_time
            from users 
            where role = 'user'
            limit #{page},#{pageSize}
            """)
    List<UserDataList> getUserDataList(int page, int pageSize);
    //查询users总数撒大大
    @Select("select count(*) from users where role='user'")
    int getUserCount();
    //查询是否存在singer这个id
    @Select("select exists(select 1 from users where user_id=#{user_id})")
    Integer isExist(@Param("user_id") Integer user_id);
    //----搜索功能
    @Select("select user_id as id,name, sex, username as email, birth_date, signature, region, avatar_path,registration_time " +
            "from users where (users.role='root' or users.role='supperRoot') AND (name like CONCAT('%', #{keyName}, '%') or users.username like concat('%' , #{keyName} , '%')) " +
            "limit #{page},#{pageSize}")
    UserDataList[] getUserDataByKeyName(String keyName, int page, int pageSize);
    //搜索出来的数据总数
    @Select("select count(*) from users where users.role='user' AND (name like CONCAT('%', #{keyName}, '%') or users.username like concat('%' , #{keyName} , '%'))")
    int getSearchUserCount(String keyName);
    //--修改用户信息
    @Update("update users set name=#{name},sex=#{sex},birth_date=#{birth_date},region=#{region},signature=#{signature} " +
            "where username=#{username}")
    void changeUsersData(@Param("username") String username, @Param("name") String name, @Param("sex") String sex, @Param("birth_date") Date birth_date, @Param("region") String region,@Param("signature") String signature);

    //管理员信息
    @Select("select user_id as id,name,sex,username as email, birth_date, signature, region, avatar_path , registration_time " +
            "from users where username=#{username} and (users.role='root' or users.role='supperRoot')")
    UserDataList getRootUserData(String username);
    //用户信息
    @Select("select user_id as id,name,sex,username as email, birth_date, signature, region, avatar_path , registration_time " +
            "from users where username=#{username} and users.role='user' ")
    UserDataList getUserData(String username);

    //----删除数据
    //直接批量删除
    @Delete("<script>" +
            "delete from users where user_id in " +
            "<foreach item='userId' collection='userIds' open='(' separator=',' close=')'>" +
            "#{userId}" +
            "</foreach>" +
            "</script>")
    void deleteUsers(@Param("userIds") List<Long> userIds);

    //----上传头像
    @Update("update users set avatar_path=#{avatar_path} where user_id=#{user_id}")
    void updateSingerPath(int user_id,String avatar_path);
    //----获取头像地址
    @Select("select avatar_path from users where user_id=#{user_id}")
    String getUserPath(int user_id);

}
