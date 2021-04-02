package com.hzp.test.controller;

import com.hzp.test.dto.PageWechatReq;
import com.hzp.test.dto.SaveWechatReq;
import com.hzp.test.dto.UpdateWechatReq;
import com.hzp.test.dto.WechatIdReq;
import com.hzp.test.entity.WechatGroup;
import com.hzp.test.service.WechatService;
import com.hzp.test.util.PageResponse;
import com.hzp.test.util.ResponseEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/manage/wechat")
@Api(tags = "微信管理")
public class WechatController {

    @Autowired
    private WechatService wechatService;

    @ApiOperation(value = "新增")
    @PostMapping(value = "/save")
    public ResponseEntity save(@RequestBody SaveWechatReq saveWechatReq) {
        wechatService.save(saveWechatReq);
        return new ResponseEntity();
    }

    @ApiOperation(value = "更新")
    @PostMapping(value = "/update")
    public ResponseEntity update(@RequestBody UpdateWechatReq updateWechatReq) {
        wechatService.update(updateWechatReq);
        return new ResponseEntity();
    }

    @ApiOperation(value = "删除")
    @PostMapping(value = "/delete")
    public ResponseEntity delete(@RequestBody WechatIdReq wechatIdReq) {
        wechatService.delete(wechatIdReq);
        return new ResponseEntity();
    }

    @ApiOperation(value = "查询单条记录")
    @PostMapping(value = "/findOne")
    public ResponseEntity<WechatGroup> findOne(@RequestBody WechatIdReq wechatIdReq) {
        return new ResponseEntity(wechatService.findOne(wechatIdReq));
    }

    @ApiOperation(value = "分页查询")
    @PostMapping(value = "/page")
    public ResponseEntity<PageResponse<WechatGroup>> page(@RequestBody PageWechatReq pageWechatReq, HttpServletRequest request) {
        System.err.println("userId : " + request.getAttribute("userId"));
        System.err.println("username : " + request.getAttribute("username"));
        return new ResponseEntity(wechatService.page(pageWechatReq));
    }

    @ApiOperation(value = "随机获取一个微信号")
    @PostMapping(value = "/getWechat")
    public ResponseEntity<String> getWechat() {
        return new ResponseEntity(wechatService.getWechat());
    }
}
