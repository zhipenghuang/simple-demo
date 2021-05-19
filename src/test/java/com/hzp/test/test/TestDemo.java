package com.hzp.test.test;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.Validator;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.html.*;
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
import com.hzp.test.util.IPUtil;
import com.hzp.test.util.HtmlUnitUtil;
import com.hzp.test.util.JwtTokenUtil;
import com.hzp.test.util.ShareCodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.*;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.*;
import java.util.stream.Collectors;

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

    //时间5分钟偏移量,2021-03-30 10:00:00代表2021-03-30 09:55:00到2021-03-30 09:59:59
    @Test
    public void strUtil() {
//        Long insert = testService.insert();

        DateTime now = DateUtil.parse("2021-03-30 09:59:59");
        int minute = now.getField(DateField.MINUTE);
        DateTime hourOfNow = DateUtil.beginOfHour(now);
        DateTime fiveMinuteKey = hourOfNow.offsetNew(DateField.MINUTE, minute - minute % 5 + 5);
        System.err.println(fiveMinuteKey.toString());
//        redisTemplate.opsForValue().set(fiveMinuteKey, "sb");

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
//        String ip = "13.75.108.60";
//        Stopwatch stopwatch = Stopwatch.createStarted();
//        System.out.println(ipUtil.getCityInfo(ip));
//        stopwatch.stop();
        System.err.println(IdUtil.randomUUID());
    }

    @Test
    public void domainTest() throws InterruptedException {

    }

    @Test
    public void crawler() {
        String url = "flcp88777.com";
        //获取指定网页实体
        HtmlPage page = HtmlUnitUtil.getHtmlPage("https://www.ce8.com/http/" + url);
        //获取所有script
        List<DomText> byXPath = (List<DomText>) page.getByXPath("//script/text()");
        //获取包含token的script
        String script = byXPath.get(2).asText();
        //最终获取token
        String token = StrUtil.subBetween(StrUtil.subAfter(script, "var token", false), "\"");
        System.err.println(token);
        HttpRequest post = HttpUtil.createPost("https://check1.ce8.com/api/check/site_all");
        post.contentType("application/json");
        Map<String, String> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("url", url);
        String paramStr = JSONUtil.parse(paramMap).toJSONString(0);
        post.body(paramStr);
        HttpResponse execute = post.execute();
        System.err.println(execute.body());
    }

    @Test
    public void crawlerBaidu() throws IOException {
        // 获取指定网页实体
        HtmlPage page = HtmlUnitUtil.getHtmlPage("https://www.baidu.com/");
        System.out.println(page.asText());  //asText()是以文本格式显示
        System.out.println(page.asXml());   //asXml()是以xml格式显示
        // 获取搜索输入框
        HtmlInput input = page.getHtmlElementById("kw");
        // 往输入框 “填值”
        input.setValueAttribute("绿林寻猫");
        // 获取搜索按钮
        HtmlInput btn = page.getHtmlElementById("su");
        // “点击” 搜索
        HtmlPage resultPage = btn.click();
        // 选择元素
        Page enclosedPage = resultPage.getEnclosingWindow().getTopWindow().getEnclosedPage();
        System.err.println(enclosedPage.getWebResponse().getContentAsString());
    }

    @Test
    public void md5File() throws IOException {
        String url = "flc371.com";
        //获取指定网页实体
        HtmlPage page = HtmlUnitUtil.getHtmlPage("https://www.boce.com/http/" + url);
//        List<HtmlInput> byXPath = (List<HtmlInput>) page.getByXPath("//input[@class=\"main02b2 banner_input_sub action_submit\"]");
//        HtmlInput htmlInput = byXPath.get(0);
//        HtmlPage click = htmlInput.click();

//        HtmlPage page1 = HtmlUnitUtil.getHtmlPage("https://www.boce.com/http/" + url);
        Page enclosedPage = page.getEnclosingWindow().getTopWindow().getEnclosedPage();
        String content = enclosedPage.getWebResponse().getContentAsString();
        System.err.println(content);
//        String s = StrUtil.subBetween(content, "<tbody class=\"node-data-tbody\">", "</tbody>");
//        List<String> collect = Arrays.stream(StrUtil.subBetweenAll(s, "<td>", "</td>")).filter(s1 -> StrUtil.contains(s1, "Kb/s")).collect(Collectors.toList());
//        System.err.println(collect.toString());
//        System.err.println(collect.size());
    }

    @Test
    public void pjs() throws InterruptedException {
        DesiredCapabilities dcaps = new DesiredCapabilities();

        dcaps.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);

        dcaps.setCapability(CapabilityType.TAKES_SCREENSHOT, false);

        dcaps.setCapability(CapabilityType.SUPPORTS_FINDING_BY_CSS, true);

        dcaps.setJavascriptEnabled(true);
//
        dcaps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY, "D:\\google download\\phantomjs-2.1.1-windows\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        PhantomJSDriver driver = new PhantomJSDriver(dcaps);

        driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
        driver.manage().timeouts().setScriptTimeout(30, TimeUnit.SECONDS);
        driver.get("https://www.boce.com/http/flc367.com");
//        WebElement button = driver.findElementByXPath("//input[@class=\"main02b2 banner_input_sub action_submit\"]");
//        System.err.println(button.getAttribute("value"));
//        button.click();
        WebElement tbody = driver.findElementByXPath("//tbody[@class=\"node-data-tbody\"]");
        List<WebElement> paixu = tbody.findElements(By.tagName("tr"));
//        for (WebElement element : paixu) {
//            List<WebElement> td = element.findElements(By.tagName("td"));
//            for (WebElement element1 : td) {
//                System.err.println(element1.getAttribute("title"));
//            }
//        }
        WebElement element = paixu.get(0);
        WebElement td = element.findElement(By.tagName("td"));
        while (StrUtil.isBlank(td.getText())) {
            td = element.findElement(By.tagName("td"));
        }
        System.err.println(td.getText());
//        File screenshotAs = driver.getScreenshotAs(OutputType.FILE);
//        File file = new File("C:\\Users\\Administrator\\Desktop\\image");
//        FileUtil.copyFilesFromDir(screenshotAs, file, true);
//        String absolutePath = file1.getAbsolutePath();
//        System.err.println(absolutePath);
//        String text = elementByXPath.getText();
//        System.err.println(text);
//        WebElement body = driver.findElement(By.tagName("body"));
//        String body_text = body.getText();
//        System.out.println(body_text);
    }
}