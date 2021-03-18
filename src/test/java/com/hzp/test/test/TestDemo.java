package com.hzp.test.test;

import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hzp.test.SimpleDemoApplication;
import com.hzp.test.dto.PageWechatReq;
import com.hzp.test.mapper.WechatGroupMapper;
import com.hzp.test.service.TestService;
import com.hzp.test.service.WechatService;
import com.hzp.test.util.JwtInfo;
import com.hzp.test.util.JwtTokenUtil;
import com.hzp.test.util.ShareCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.HashSet;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SimpleDemoApplication.class)
public class TestDemo {

    @Autowired
    private WechatService wechatService;
    @Autowired
    private WechatGroupMapper wechatGroupMapper;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private TestService testService;


    @Test
    public void tes() {
        PageWechatReq pageWechatReq = new PageWechatReq();
        pageWechatReq.setGroup("y");
        pageWechatReq.setPageIndex(1);
        pageWechatReq.setPageSize(2);
        wechatService.page(pageWechatReq);
    }

    @Test
    public void guavaCacheTest() {
        Cache<String, List<String>> cache = CacheBuilder.newBuilder().build();
//        cache.put("name", Lists.newArrayList("123", "456"));
        cache.invalidate("wechats");
    }

    private static final int threads = 100;

    @Test
    public void test2() throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(threads);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(threads, threads, 10, TimeUnit.SECONDS, new LinkedBlockingQueue<>(threads),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
        executor.prestartAllCoreThreads();
        for (int i = 0; i < threads; i++) {
            executor.submit(() -> {
                String wechat = wechatService.getWechat();
                log.info(wechat);
                countDownLatch.countDown();
            });
        }
        //等待计算线程执行完
        countDownLatch.await();
//        for (int i = 0; i < 10; i++) {
//            String wechat = wechatService.getWechat();
//            log.info(wechat);
//        }
        executor.shutdown();
    }

    @Test
    public void jwtTest() {
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setUserId(1111L);
        jwtInfo.setUsername("yunmu");

        String token = jwtTokenUtil.generateToken(jwtInfo);
        System.err.println(token);

        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            JwtInfo info = jwtTokenUtil.validateToken(token);
            stopwatch.stop();
            System.err.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            System.err.println(info);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void hutoolTest() {
//        String seckey = IdUtil.fastSimpleUUID();
//        System.err.println(seckey);
//        String md5Str = SecureUtil.md5("hzp172594");
//        System.err.println(md5Str);
//        String hzp172594 = DigestUtil.md5Hex("hzp172594");
//        System.err.println(hzp172594);
//        System.err.println(Validator.isMatchRegex("^[A-Za-z0-9]{6,16}+$", "0123456789123456"));
//        System.err.println(Validator.isMatchRegex("([\\u4e00-\\u9fa5]){1,5}", "我是中国人"));
        Stopwatch stopwatch = Stopwatch.createStarted();
        String s1 = DigestUtil.bcrypt("hzp172594");
//        String hzp172594 = DigestUtil.md5Hex("hzp172594");
        stopwatch.stop();
        System.err.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
        System.err.println(s1);
//        String s2 = DigestUtil.bcrypt("hzp172594");
//        System.err.println(s2);
//        System.err.println(DigestUtil.bcryptCheck("hzp172594", s1));
//        System.err.println(DigestUtil.bcryptCheck(s1,s2));
    }

    @Test
    public void te1() {
        Integer type = testService.getType();
        System.err.println(type);
    }

    @Test
    public void shareCodeTest() {
//        String s = ShareCodeUtil.toSerialCode(123L);
//        System.err.println(s);
//        long l = ShareCodeUtil.codeToId(s);
//        System.err.println(l);
        HashSet<String> set = new HashSet<>();
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (long i = 40000000; i < 60000000; i++) {
            String shareCode = ShareCodeUtil.toSerialCode(i);
            set.add(shareCode);
        }
        stopwatch.stop();
        System.err.println(set.size());
        System.err.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }
}
