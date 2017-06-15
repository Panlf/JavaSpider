package com.dao;

import com.pojo.Novel;
import com.pojo.NovelExample;
import java.util.List;
import org.apache.ibatis.annotations.Param;

public interface NovelMapper {
    int countByExample(NovelExample example);

    int deleteByExample(NovelExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Novel record);

    int insertSelective(Novel record);

    List<Novel> selectByExampleWithBLOBs(NovelExample example);

    List<Novel> selectByExample(NovelExample example);

    Novel selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Novel record, @Param("example") NovelExample example);

    int updateByExampleWithBLOBs(@Param("record") Novel record, @Param("example") NovelExample example);

    int updateByExample(@Param("record") Novel record, @Param("example") NovelExample example);

    int updateByPrimaryKeySelective(Novel record);

    int updateByPrimaryKeyWithBLOBs(Novel record);

    int updateByPrimaryKey(Novel record);
}