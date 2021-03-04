package com.hzp.test.mapper;

import com.hzp.test.entity.Password;
import com.hzp.test.entity.PasswordExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface PasswordMapper {

    int countByExample(PasswordExample example);

    int deleteByExample(PasswordExample example);

    int insert(Password record);

    int insertSelective(Password record);

    Password selectOneByExample(PasswordExample example);

    List<Password> selectByExample(PasswordExample example);

    int updateByExampleSelective(@Param("record") Password record, @Param("example") PasswordExample example);

    int updateByExample(@Param("record") Password record, @Param("example") PasswordExample example);
}