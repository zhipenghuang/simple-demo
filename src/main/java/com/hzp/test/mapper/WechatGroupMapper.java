package com.hzp.test.mapper;

import com.hzp.test.entity.WechatGroup;
import com.hzp.test.entity.WechatGroupExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface WechatGroupMapper {

    int countByExample(WechatGroupExample example);

    int deleteByExample(WechatGroupExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(WechatGroup record);

    int insertSelective(WechatGroup record);

    WechatGroup selectOneByExample(WechatGroupExample example);

    List<WechatGroup> selectByExample(WechatGroupExample example);

    WechatGroup selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") WechatGroup record, @Param("example") WechatGroupExample example);

    int updateByExample(@Param("record") WechatGroup record, @Param("example") WechatGroupExample example);

    int updateByPrimaryKeySelective(WechatGroup record);

    int updateByPrimaryKey(WechatGroup record);

}