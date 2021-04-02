package com.hzp.test.controller;

import com.hzp.test.dto.LoginReq;
import com.hzp.test.service.LoginService;
import com.hzp.test.dto.common.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/login")
@Api(tags = "登录")
public class LoginController {

    @Autowired
    private LoginService loginService;


    @ApiOperation(value = "登录")
    @PostMapping(value = "")
    public ResponseEntity save(@RequestBody LoginReq loginReq) {
        loginService.login(loginReq);
        return new ResponseEntity();
    }
}
