package com.hzp.test.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.StrUtil;
import com.google.common.base.Stopwatch;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.util.concurrent.RateLimiter;
import com.hzp.test.SimpleDemoApplication;
import com.hzp.test.dto.LoginReq;
import com.hzp.test.dto.PageWechatReq;
import com.hzp.test.dto.common.JwtInfo;
import com.hzp.test.mapper.WechatGroupMapper;
import com.hzp.test.service.TestService;
import com.hzp.test.service.WechatService;
import com.hzp.test.util.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
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
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private RedisTemplate redisTemplate;

    private IPUtil ipUtil;

    @Before
    public void setUp() {
        ipUtil = new IPUtil();
    }

    @After
    public void tearDown() {
        ipUtil = null;
    }

    @Test
    public void page() {
        PageWechatReq pageWechatReq = new PageWechatReq();
        pageWechatReq.setGroup("y");
        pageWechatReq.setPageIndex(1);
        pageWechatReq.setPageSize(2);
        wechatService.page(pageWechatReq);
    }

    @Test
    public void guavaCache() {
        Cache<String, List<String>> cache = CacheBuilder.newBuilder().build();
//        cache.put("name", Lists.newArrayList("123", "456"));
        cache.invalidate("wechats");
    }

    private static final int threads = 100;

    @Test
    public void mutiThread() throws InterruptedException {
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
    public void jwt() {
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
    public void hutool() {
//        String seckey = IdUtil.simpleUUID();
//        System.err.println(seckey);
//
//        String md5Str = SecureUtil.md5("hzp172594");
//        System.err.println(md5Str);
//        String md5Hex = DigestUtil.md5Hex("hzp172594");
//        System.err.println(md5Hex);
//
//        System.err.println(Validator.isMatchRegex("^[A-Za-z0-9]{6,16}+$", "0123456789123456"));
//        System.err.println(Validator.isMatchRegex("([\\u4e00-\\u9fa5]){1,5}", "我是中国人"));
        System.err.println(Validator.isMatchRegex("^[0-9]{8}+$", "023456789"));
//        String s2 = DigestUtil.bcrypt("hzp172594");
//        System.err.println(s2);
//        System.err.println(DigestUtil.bcryptCheck("hzp172594", s2));
    }

    @Test
    public void performance() {
        JwtInfo jwtInfo = new JwtInfo();
        jwtInfo.setUserId(1111L);
        jwtInfo.setUsername("yunmu");

        String token = jwtTokenUtil.generateToken(jwtInfo);
        jwtTokenUtil.validateToken(token);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (int i = 0; i < 1000; i++) {
            long elapsed = stopwatch.elapsed(TimeUnit.MICROSECONDS);
            jwtTokenUtil.validateToken(token);
            System.err.println(i + " :" + (stopwatch.elapsed(TimeUnit.MICROSECONDS) - elapsed) + "微秒");
        }
        stopwatch.stop();
        System.err.println(stopwatch.elapsed(TimeUnit.MILLISECONDS) + " 毫秒");
    }

    @Test
    public void shareCode() {
//        String s = ShareCodeUtil.toSerialCode(123L);
//        System.err.println(s);
//        long l = ShareCodeUtil.codeToId(s);
//        System.err.println(l);
        HashSet<String> set = new HashSet<>();
        ShareCodeUtil.toSerialCode(15);
        Stopwatch stopwatch = Stopwatch.createStarted();
        for (long i = 0; i < 10000; i++) {
            String shareCode = ShareCodeUtil.toSerialCode(150);
            System.err.println(shareCode);
            System.err.println(ShareCodeUtil.codeToId(shareCode));
            set.add(shareCode);
        }
        stopwatch.stop();
        System.err.println(set.size());
        System.err.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
    }

    @Test
    public void rateLimit() {
        RateLimiter rateLimiter = RateLimiter.create(1);
        for (int i = 1; i <= 5; i++) {
            double waitTime = rateLimiter.acquire(i);
            System.out.println("cutTime=" + System.currentTimeMillis() + " acq:" + i + " waitTime:" + waitTime);
        }
    }

    @Test
    public void dateUtil() {
        DateTime yesterday = DateUtil.yesterday();
        System.err.println(yesterday);
    }

    @Test
    public void redis() throws IOException {
        Object execute = redisTemplate.execute((RedisCallback<Set<String>>) connection -> {
            Set<String> result = new HashSet<>();
            //cursor用完要close，这里利用Java7新特性的自动close它
            try (Cursor<byte[]> cursor = connection.scan(ScanOptions.scanOptions().match("access_token*").count(1).build())) {
                while (cursor.hasNext()) {
                    byte[] bytes = cursor.next();
                    result.add(new String(bytes, StandardCharsets.UTF_8));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return result;
        });
        System.out.println(execute);
    }

    @Test
    public void jsonUtil() {
        LoginReq loginReq = new LoginReq();
        loginReq.setPassword("123456");
//        String s = JSONUtil.parse(loginReq).toJSONString(0);
//        System.err.println(s);
//        System.err.println(JSONUtil.toBean(s, LoginReq.class));

//        Object ceshi = redisTemplate.opsForValue().get("ceshi");
//        ObjectMapper objectMapper = new ObjectMapper();
//        System.err.println(objectMapper.convertValue(ceshi, LoginReq.class));
        Map<String, LoginReq> map = new HashMap<>();
        map.put("ssss", loginReq);
        redisTemplate.opsForValue().set("ceshi", map);
    }

    @Test
    public void httpUtil() {
        try {
            Stopwatch stopwatch = Stopwatch.createStarted();
            Map<String, String> s = HttpUtils.ipParse("104.193.88.123");
            stopwatch.stop();
            System.err.println(stopwatch.elapsed(TimeUnit.MILLISECONDS));
            System.err.println(s);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //时间5分钟偏移量,2021-03-30 10:00:00代表2021-03-30 09:55:00到2021-03-30 09:59:59
    @Test
    public void strUtil() {
//        Long insert = testService.insert();

        DateTime now = DateUtil.parse("2021-03-30 09:59:59");
        int minute = now.getField(DateField.MINUTE);
        DateTime hourOfNow = DateUtil.beginOfHour(now);
        DateTime fiveMinuteKey = hourOfNow.offsetNew(DateField.MINUTE, minute - minute % 5 + 5);
        System.err.println(fiveMinuteKey);
        redisTemplate.opsForValue().set(fiveMinuteKey, "sb");

//        String template = "{}爱{}，就像老鼠爱大米";
//        String str = StrUtil.format(template, "我", "你"); //str -> 我爱你，就像老鼠爱大米
//        System.err.println(str);
    }

    @Test
    public void clearCache() {
//        Set keys = redisTemplate.keys("*");
//        Iterator iterator = keys.iterator();
//        while (iterator.hasNext()) {
////            redisTemplate.delete(iterator.next());
//            System.err.println(iterator.next());
//        }
//        byte[] body = new byte[]{1,56,35,89};
//        ByteArrayInputStream bais = new ByteArrayInputStream(body);
//        System.err.println(bais.read());
//        System.err.println(bais.read());
//        System.err.println(bais.read());
//        System.err.println(bais.read());
//        System.err.println(bais.read());
        String retStr = null;
        String sStr = "4";
        int i = StrUtil.indexOfIgnoreCase(retStr, sStr);
        System.err.println(i);
    }

    @Test
    public void ipTest() {
        String ip = "183.178.28.130";
        Stopwatch stopwatch = Stopwatch.createStarted();
        System.out.println(ipUtil.getCityInfo(ip));
        stopwatch.stop();
        System.err.println(stopwatch.elapsed(TimeUnit.MICROSECONDS));
    }
}
