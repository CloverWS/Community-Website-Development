package com.nowcoder.community.util;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.DigestUtils;

import java.util.HashMap;
import java.util.Map;
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

    public static String getJSONString(int code, String msg, Map<String, Object> map){
        JSONObject json = new JSONObject();
        json.put("code",code);
        json.put("msg", msg);
        if(map != null){
            for (String key : map.keySet()){
                json.put(key, map.get(key));
            }
        }
        return json.toJSONString();
    }

    public static String getJSONString(int code, String msg){
        return getJSONString(code, msg,null);
    }

    public static String getJSONString(int code){
        return getJSONString(code,null,null);
    }


//    -----------------------test--------------------------------------------
    public static void main(String[] args){
        Map<String, Object> map = new HashMap<>();
        map.put("name","zhangsan");
        map.put("age",25);
        System.out.println(getJSONString(0,"ok",map));
    }
//    -------------------------------------------------------------------
}
