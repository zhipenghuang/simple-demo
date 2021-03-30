package com.hzp.test.service.impl;

import com.hzp.test.dto.LoginReq;
import com.hzp.test.entity.Password;
import com.hzp.test.entity.PasswordExample;
import com.hzp.test.exception.ParamsException;
import com.hzp.test.exception.SysException;
import com.hzp.test.exception.SystemErrors;
import com.hzp.test.mapper.PasswordMapper;
import com.hzp.test.service.LoginService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoginServiceImpl implements LoginService {

    @Autowired
    private PasswordMapper passwordMapper;

    @Override
    public void login(LoginReq loginReq) {
        Password password = passwordMapper.selectOneByExample(new PasswordExample());
        if (password == null || StringUtils.isBlank(password.getPassword())) {
            throw new SysException(SystemErrors.PASSWORD_NOT_EXISTS);
        }
        if (StringUtils.isBlank(loginReq.getPassword()) || !password.getPassword().equals(loginReq.getPassword())) {
            throw new ParamsException(SystemErrors.PASSWORD_ERROR);
        }
    }
}
