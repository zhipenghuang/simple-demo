package com.hzp.test.service.impl;

import com.hzp.test.entity.Test;
import com.hzp.test.mapper.TestMapper;
import com.hzp.test.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestServiceImpl implements TestService {

    @Autowired
    private TestMapper testMapper;


    @Override
    public Integer getType() {
        Test test = testMapper.selectByPrimaryKey(1L);
        return test.getNum();
    }

    @Override
    public Long insert() {
        Test test =new Test();
        test.setNum(52);
        int insert = testMapper.insert(test);
        System.err.println(insert);
        System.err.println(test.getId());
        return test.getId();
    }
}
