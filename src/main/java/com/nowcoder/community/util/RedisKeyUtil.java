package com.nowcoder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    // 点赞常量
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    // 某个User的赞
    private static final String PREFIX_USER_LIKE = "like:user";
    // 关注的目标followee（关注谁）
    private static final String PREFIX_FOLLOWEE = "followee";
    // 关注者follower(粉丝)
    private static final String PREFIX_FOLLOWER = "follower";
    // 验证码
    private static final String PREFIX_KAPTCHA = "kapatcha";
    // 登录凭证
    private static final String PREFIX_TICKET = "ticket";
    // 登录
    private static final String PREFIX_USER = "user";
    // 日访问用户（无论有无登录）
    private static final String PREFIX_UV = "uv";
    // 日活跃用户
    private static final String PREFIX_DAU = "dau";
    // 帖子缓存--计算帖子分数的key
    private static final String PREFIX_POST = "post";

   // 某个实体的赞
   // 目标 ： like:entity:entityType:entityId -> set(userId)
   // 准备key
   public static String getEntityLikeKey(int entityType, int entityId){
       return PREFIX_ENTITY_LIKE + SPLIT + entityType + SPLIT + entityId;
   }

     // 某个用户的赞
    // 目标 ： like:user:userId -> set(userId)
    // 准备key
     public static String getUserLikeKey(int userId){
         return PREFIX_USER_LIKE + SPLIT + userId;
     }

     // 某个用户关注的实体
    // followee:userId:entityType -> zset(entityId,now)
    public static String getFolloweeKey(int userId, int entityType){
       return PREFIX_FOLLOWEE + SPLIT + userId + SPLIT + entityType;
    }

    // 某个用户拥有的粉丝
    // follower:entityType:entityId -> zset(userId,now)
    public static String getFollowerKey(int entityType, int entityId){
        return PREFIX_FOLLOWER + SPLIT + entityType + SPLIT + entityId;
    }

    // 登录验证码
    public static String getKapatchaKey(String owner){
        return PREFIX_KAPTCHA + SPLIT + owner;
    }

    // 登录凭证
    public static String getTicketKey(String ticket){
        return PREFIX_TICKET + SPLIT + ticket;
    }

    // 登录
    public static String getUserKey(int userId){
        return PREFIX_USER + SPLIT + userId;
    }

    // 单日UV
    public static String getUVKey(String data){
        return PREFIX_UV + SPLIT + data;
    }

    // 区间UV
    public static String getUVKey(String startData,String endData){
        return PREFIX_UV + SPLIT + startData + SPLIT + endData;
    }

    // 单日DAU
    public static String getDAUKey(String data){
        return PREFIX_DAU + SPLIT + data;
    }

    // 区间DAU
    public static String getDAUKey(String startData,String endData){
        return PREFIX_DAU + SPLIT + startData + SPLIT + endData;
    }

    // 帖子分数
    public static String getPostScoreKey(){
        return PREFIX_POST + SPLIT + "score";
    }





}
