package com.hzp.test.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.google.common.base.Splitter;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hzp.test.exception.ParamsException;
import com.hzp.test.service.WechatService;
import com.hzp.test.dto.PageWechatReq;
import com.hzp.test.dto.SaveWechatReq;
import com.hzp.test.dto.UpdateWechatReq;
import com.hzp.test.dto.WechatIdReq;
import com.hzp.test.entity.WechatGroup;
import com.hzp.test.entity.WechatGroupExample;
import com.hzp.test.exception.SysException;
import com.hzp.test.exception.SystemErrors;
import com.hzp.test.mapper.WechatGroupMapper;
import com.hzp.test.dto.common.PageResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class WechatServiceImpl implements WechatService {

    private static final String CACHE_KEY = "wechats";
    private final Cache<String, List<String>> cache;

    public WechatServiceImpl() {
        cache = CacheBuilder.newBuilder().build();
    }

    @Autowired
    private WechatGroupMapper wechatGroupMapper;

    @Override
    public void save(SaveWechatReq saveWechatReq) {
        if (StringUtils.isEmpty(saveWechatReq.getGroup()) || StringUtils.isEmpty(saveWechatReq.getWechats())) {
            throw new SysException(SystemErrors.GROUP_WECHAT_NOT_NULL);
        }
        if (saveWechatReq.getWechats().contains("，")) {
            throw new SysException(SystemErrors.CN_CHAR_EXISTS);
        }
        WechatGroupExample example = new WechatGroupExample();
        example.createCriteria().andGroupEqualTo(saveWechatReq.getGroup());
        WechatGroup wechatGroup1 = wechatGroupMapper.selectOneByExample(example);
        if (wechatGroup1 != null) {
            throw new SysException(SystemErrors.GROUP_EXISTS);
        }
        WechatGroup wechatGroup = new WechatGroup();
        wechatGroup.setGroup(saveWechatReq.getGroup());
        wechatGroup.setWechats(saveWechatReq.getWechats());
        wechatGroup.setCreateTime(new Date());
        wechatGroup.setUpdateTime(new Date());
        wechatGroupMapper.insert(wechatGroup);
        cache.invalidate(CACHE_KEY);
    }

    @Override
    public void update(UpdateWechatReq updateWechatReq) {
        if (updateWechatReq.getId() == null) {
            throw new ParamsException(SystemErrors.ID_NOT_NULL);
        }
        if (StringUtils.isNotBlank(updateWechatReq.getWechats()) && updateWechatReq.getWechats().contains("，")) {
            throw new SysException(SystemErrors.CN_CHAR_EXISTS);
        }
        WechatGroupExample example = new WechatGroupExample();
        example.createCriteria().andGroupEqualTo(updateWechatReq.getGroup()).andIdNotEqualTo(updateWechatReq.getId());
        WechatGroup wechatGroup1 = wechatGroupMapper.selectOneByExample(example);
        if (wechatGroup1 != null) {
            throw new SysException(SystemErrors.GROUP_EXISTS);
        }
        WechatGroup wechatGroup = new WechatGroup();
        wechatGroup.setId(updateWechatReq.getId());
        wechatGroup.setGroup(updateWechatReq.getGroup());
        wechatGroup.setWechats(updateWechatReq.getWechats());
        wechatGroup.setUpdateTime(new Date());
        wechatGroupMapper.updateByPrimaryKeySelective(wechatGroup);
        cache.invalidate(CACHE_KEY);
    }

    @Override
    public void delete(WechatIdReq wechatIdReq) {
        if (wechatIdReq.getId() == null) {
            throw new ParamsException(SystemErrors.ID_NOT_NULL);
        }
        wechatGroupMapper.deleteByPrimaryKey(wechatIdReq.getId());
        cache.invalidate(CACHE_KEY);
    }

    @Override
    public WechatGroup findOne(WechatIdReq wechatIdReq) {
        if (wechatIdReq.getId() == null) {
            throw new ParamsException(SystemErrors.ID_NOT_NULL);
        }
        WechatGroup wechatGroup = wechatGroupMapper.selectByPrimaryKey(wechatIdReq.getId());
        return wechatGroup;
    }

    @Override
    public PageResponse<WechatGroup> page(PageWechatReq req) {
        WechatGroupExample example = new WechatGroupExample();
        WechatGroupExample.Criteria criteria = example.createCriteria();
        if (StringUtils.isNotBlank(req.getGroup())) {
            criteria.andGroupLike("%" + req.getGroup() + "%");
        }
        if (StringUtils.isNotBlank(req.getWechats())) {
            criteria.andWechatsLike("%" + req.getWechats() + "%");
        }
        PageHelper.startPage(req.getPageIndex(), req.getPageSize(), "create_time desc");
        PageInfo<WechatGroup> info = new PageInfo<>(wechatGroupMapper.selectByExample(example));
        PageResponse<WechatGroup> pages = new PageResponse();
        pages.setPageIndex(req.getPageIndex());
        pages.setPageSize(req.getPageSize());
        pages.setTotal((int) info.getTotal());
        pages.setData(info.getList());
        return pages;
    }


    @Override
    public String getWechat() {
        List<String> wechatsCache = cache.getIfPresent(CACHE_KEY);
        if (CollectionUtils.isEmpty(wechatsCache)) {
            log.info("微信号走db");
            WechatGroupExample example = new WechatGroupExample();
            List<WechatGroup> list = wechatGroupMapper.selectByExample(example);
            if (CollectionUtils.isEmpty(list)) {
                return "";
            }
            List<String> wechats = new ArrayList<>();
            for (WechatGroup wechatGroup : list) {
                wechats.addAll(Splitter.on(",").splitToList(wechatGroup.getWechats()));
            }
            if (CollectionUtils.isEmpty(wechats)) {
                return "";
            }
            cache.put(CACHE_KEY, wechats);
            return wechats.get(RandomUtil.getSecureRandom().nextInt(wechats.size()));
        }
        log.info("微信号走cache");
        return wechatsCache.get(RandomUtil.getSecureRandom().nextInt(wechatsCache.size()));
    }
}
