package org.example.commoncore.utils;

import java.util.UUID;

public class DigestUtils {

    public static String generateSessionId(int userId) {
        String raw = userId + "_" + UUID.randomUUID() + "_" + System.currentTimeMillis();
        return MD5Utils.generateMd5Str(raw); // 你已有的工具类
    }

}
