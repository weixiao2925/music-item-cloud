package org.example.commoncore.entity;

import com.alibaba.fastjson2.JSONObject;
import com.alibaba.fastjson2.JSONWriter;

//返回数据处理
public record RestBean<T>(int code, T data, String message) {
    public  static <T> RestBean<T>  success(T data){
        return new RestBean<>(200,data,"请求成功");
    }

    public static <T> RestBean<T> redirect(T url){ return new RestBean<>(302, url, "重定向请求"); }

    public static <T> RestBean<T> success(){
        return success(null);
    }
//统一401
    public static <T> RestBean<T> unauthorized(String message){
        return failure(401,message);
    }
//统一403
    public static <T> RestBean<T> forbidden(String message){
        return failure(403,message);
    }
    public  static <T> RestBean<T> failure(int code,String message){
        return new RestBean<>(code,null,message);
    }

    public String asJsonString(){
        return JSONObject.toJSONString(this, JSONWriter.Feature.WriteNulls);
    }
// 统一460
    public static <T> RestBean<T> tokenExpired(String message){
        return failure(460, message);
    }

    public static <T> RestBean<T> tokenInvalid(String message){
        return failure(461,message);
    }
}
