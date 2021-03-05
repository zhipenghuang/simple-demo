package com.hzp.test.test;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.hzp.test.SimpleDemoApplication;
import com.hzp.test.dto.PageWechatReq;
import com.hzp.test.mapper.WechatGroupMapper;
import com.hzp.test.service.WechatService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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


    @Test
    public void tes() {
        PageWechatReq pageWechatReq = new PageWechatReq();
        pageWechatReq.setGroup("y");
        pageWechatReq.setPageIndex(1);
        pageWechatReq.setPageSize(2);
        wechatService.page(pageWechatReq);
    }

    @Test
    public void tes1() {
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
}
