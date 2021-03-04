package com.hzp.test.controller;

import com.hzp.test.service.DomainService;
import com.hzp.test.util.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/manage/domain")
@Api(tags = "域名")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @ResponseBody
    @RequestMapping(value = "findOne", method = RequestMethod.POST)
    @ApiOperation(value = "获取域名")
    public ResponseEntity<String> findOne() {
        return new ResponseEntity(domainService.findOne());
    }

}
