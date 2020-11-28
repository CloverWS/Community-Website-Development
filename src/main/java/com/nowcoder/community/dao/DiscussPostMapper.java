package com.nowcoder.community.dao;


import com.nowcoder.community.entity.DiscussPost;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface DiscussPostMapper {

    List<DiscussPost> selectDiscussPosts(int userId, int offset, int limit); // offset 起始ID， limit 每页显示多少数据

    int selectDiscussPostRows(@Param("userId") int userId); //@Param("alias") 当参数名太长了，可以用@Param取别名。
    //如果只有一个参数，并且在<if>里使用，则必须加别名

}