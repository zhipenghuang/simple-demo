package com.hzp.test.service.impl;

import com.hzp.test.service.DomainService;
import com.hzp.test.entity.Domain;
import com.hzp.test.entity.DomainExample;
import com.hzp.test.exception.SysException;
import com.hzp.test.exception.SystemErrors;
import com.hzp.test.mapper.DomainMapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DomainServiceImpl implements DomainService {

    @Autowired
    private DomainMapper domainMapper;

    @Override
    public String findOne() {
        Domain domain = domainMapper.selectOneByExample(new DomainExample());
        if (domain == null || StringUtils.isBlank(domain.getDomain())) {
            throw new SysException(SystemErrors.NO_DOMAIN);
        }
        return domain.getDomain();
    }
}
