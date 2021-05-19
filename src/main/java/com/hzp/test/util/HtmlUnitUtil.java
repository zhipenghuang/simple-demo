package com.hzp.test.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * 模拟点击，动态获取页面信息
 *
 * @Author: Uncle liu
 * @Date: 2019-10-07 15:39
 */
public class HtmlUnitUtil {

    private static class innerWebClient {
        private static final WebClient webClient = new WebClient(BrowserVersion.INTERNET_EXPLORER_7);
    }

    /**
     * 获取指定网页实体
     *
     * @param url
     */
    public static HtmlPage getHtmlPage(String url) {
        //调用此方法时加载WebClient
        WebClient webClient = innerWebClient.webClient;

        webClient.setJavaScriptEnabled(false); //启用JS解释器，默认为true
        webClient.setJavaScriptTimeout(10000);//设置JS执行的超时时间
        webClient.setCssEnabled(false); //禁用css支持
        webClient.setThrowExceptionOnScriptError(false); //js运行错误时，是否抛出异常
        webClient.setTimeout(10000); //设置连接超时时间 ，这里是10S。如果为0，则无限期等待
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());//设置支持AJAX
        webClient.waitForBackgroundJavaScript(30000);
        HtmlPage page = null;
        try {
            // 获取指定网页实体
            page = (HtmlPage) webClient.getPage(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }
}
