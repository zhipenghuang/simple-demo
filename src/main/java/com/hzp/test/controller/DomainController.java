package com.hzp.test.controller;

import com.hzp.test.service.DomainService;
import com.hzp.test.util.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/domain")
@Api(tags = "域名")
public class DomainController {

    @Autowired
    private DomainService domainService;

    @ApiOperation(value = "获取域名")
    @PostMapping(value = "findOne")
    public ResponseEntity<String> findOne(HttpServletRequest request) {
        System.err.println("userId : "+ request.getAttribute("userId"));
        System.err.println("username : "+ request.getAttribute("username"));
        return new ResponseEntity(domainService.findOne());
    }

}
