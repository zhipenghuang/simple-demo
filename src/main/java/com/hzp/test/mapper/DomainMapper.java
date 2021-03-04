package com.hzp.test.mapper;

import com.hzp.test.entity.Domain;
import com.hzp.test.entity.DomainExample;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface DomainMapper {

    int countByExample(DomainExample example);

    int deleteByExample(DomainExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(Domain record);

    int insertSelective(Domain record);

    Domain selectOneByExample(DomainExample example);

    List<Domain> selectByExample(DomainExample example);

    Domain selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") Domain record, @Param("example") DomainExample example);

    int updateByExample(@Param("record") Domain record, @Param("example") DomainExample example);

    int updateByPrimaryKeySelective(Domain record);

    int updateByPrimaryKey(Domain record);
}