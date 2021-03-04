package com.hzp.test.service;

import com.hzp.test.dto.PageWechatReq;
import com.hzp.test.dto.WechatIdReq;
import com.hzp.test.dto.SaveWechatReq;
import com.hzp.test.dto.UpdateWechatReq;
import com.hzp.test.entity.WechatGroup;
import com.hzp.test.util.PageResult;

public interface WechatService {
    void save(SaveWechatReq saveWechatReq);

    void update(UpdateWechatReq updateWechatReq);

    void delete(WechatIdReq wechatIdReq);

    WechatGroup findOne(WechatIdReq wechatIdReq);

    PageResult<WechatGroup> page(PageWechatReq pageWechatReq);

    String getWechat();

}
