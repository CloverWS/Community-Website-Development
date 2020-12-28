package com.nowcoder.community.util;

public class RedisKeyUtil {

    private static final String SPLIT = ":";
    // 点赞常量
    private static final String PREFIX_ENTITY_LIKE = "like:entity";
    // 某个User的赞
    private static final String PREFIX_USER_LIKE = "like:user";

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



}
