package org.example.commoncore.constants;


import java.time.format.DateTimeFormatter;

//存入经常使用的属性
public class Const {
    // ---Cookie相关
    public static final String SESSION_ID = "session_id";

    //---jwt相关
    public static final String JWT_REFRESH_TOKEN_PREFIX = "jwt:refresh:token:prefix:%s";
    public static final String SESSION_ACCESS = "session:access:";
    public static final String SESSION_REFRESH = "session:refresh:";

    //邮箱验证码存入Redis前缀
    public static final String VERIFY_EMAIL_DATA="verify:email:";

    //黑名单前缀
    public static final String JWT_BLACK_LIST="jwt:blacklist:";

    //用户id（在拦截器时就存在了setAttribute里面）
    public static final String ATTR_USER_ID="user_id:";
    //用户名称
    public static final String ATTR_USERNAME="username:";

    //---用户角色
    public static final String ROLE_DEFAULT="user";
    public static final String ROLE_ADMIN="admin";

    //用户封禁
    public static final String BANNED_BLOCK="banned:block:";

    //跨域拦截器的优先级
    public static final int ORDER_CORS=-102;

    //存头像的文件地址（歌手，歌单，用户，歌曲）
    public static final String AVATAR_PATH="D:\\item2\\avatar\\";

    //存mp3文件地址
    public static  final String MP3_PATH="D:\\item2\\mp3\\";

    //存lrc文件地址
    public static final String LRC_PATH="D:\\item2\\lrc\\";

    // Minio相关
    public static final String MINIO_READING = "/reading/";

    // 类型相关
    public static final String WATER = "water";
    public static final String ELECTRICITY = "electricity";
    public static final String GAS = "gas";

    // 时间相关
    public static final DateTimeFormatter MONTH_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM");
}
