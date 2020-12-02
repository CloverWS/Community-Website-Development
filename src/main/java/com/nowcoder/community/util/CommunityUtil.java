package com.nowcoder.community.util;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.UUID;

public class CommunityUtil {

    // Create a random String
    public static String generateUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    // MD5
    // hello -> abc123def456 只能加密，不能解密，且每次加密都是一样的. 所以需要加盐salt
    public static String md5(String key){
        if (StringUtils.isBlank(key)){
            return null;
        }else{
            // DigestUtils是Spring自带的生成MD5并且加盐的工具
            return DigestUtils.md5DigestAsHex(key.getBytes());
        }
    }
}
