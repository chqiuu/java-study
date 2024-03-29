package com.chqiuu.study.httpclient5;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.hc.client5.http.classic.methods.HttpGet;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.DefaultSchemePortResolver;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.CloseableHttpResponse;
import org.apache.hc.client5.http.impl.classic.HttpClientBuilder;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManagerBuilder;
import org.apache.hc.client5.http.impl.routing.DefaultRoutePlanner;
import org.apache.hc.client5.http.io.HttpClientConnectionManager;
import org.apache.hc.client5.http.ssl.NoopHostnameVerifier;
import org.apache.hc.client5.http.ssl.SSLConnectionSocketFactory;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.ParseException;
import org.apache.hc.core5.http.io.entity.EntityUtils;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.ssl.SSLContexts;
import org.apache.hc.core5.ssl.TrustStrategy;
import org.junit.jupiter.api.Test;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.net.InetAddress;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class HttpClientTest {

    @Test
    void get() {
        String url = "https://blog.csdn.net/QIU176161650/article/details/118180822";
        url = "https://gitee.com/";
        url = "https://bm.ruankao.org.cn/";
        url = "https://github.com/chqiuu";
        url = "https://www.it-teaching.com/";
        String html = NetworkUtil.get(url);
        System.out.println(html);
    }

    @Test
    void getLocalAddress() {
        String localIp = "192.168.2.103";
        String url = "https://blog.csdn.net/QIU176161650/article/details/118180822";
        url = "http://192.168.2.1";
        String html = NetworkUtil.get(url, localIp);
        System.out.println(html);
    }

    @Test
    void httpClient5Connect() {
        String localAddress = "192.168.2.103";
        String[] ipStr = localAddress.split("\\.");
        byte[] localAddressByte = new byte[4];
        for (int i = 0; i < 4; i++) {
            localAddressByte[i] = (byte) (Integer.parseInt(ipStr[i]) & 0xff);
        }
        RequestConfig config = RequestConfig.custom().build();
        HttpGet httpGet = new HttpGet("http://192.168.2.1/");
        httpGet.setConfig(config);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        // 关键代码，重写DefaultRoutePlanner.determineLocalAddress 方法，加入需绑定的本地IP
        httpClientBuilder.setRoutePlanner(new DefaultRoutePlanner(DefaultSchemePortResolver.INSTANCE) {
            @SneakyThrows
            @Override
            protected InetAddress determineLocalAddress(final HttpHost firstHop, final HttpContext context) {
                return InetAddress.getByAddress(localAddressByte);
            }
        });
        try (CloseableHttpClient httpClient = httpClientBuilder.build()) {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity(), "UTF-8");
            log.info(body);
            // TODO 获取页面内容实现
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    void httpClient5SslConnect() {
        RequestConfig config = RequestConfig.custom().build();
        HttpGet httpGet = new HttpGet("https://blog.csdn.net/QIU176161650/article/details/118388848");
        httpGet.setConfig(config);
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        try (CloseableHttpClient httpClient = httpClientBuilder.setConnectionManager(getHttpClientConnectionManager()).build()) {
            CloseableHttpResponse response = httpClient.execute(httpGet);
            String body = EntityUtils.toString(response.getEntity(), "UTF-8");
            log.info(body);
            // TODO 获取页面内容实现
        } catch (IOException | ParseException | NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            e.printStackTrace();
        }
    }

    private static HttpClientConnectionManager getHttpClientConnectionManager() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return PoolingHttpClientConnectionManagerBuilder.create()
                .setSSLSocketFactory(getSslConnectionSocketFactory())
                .build();
    }

    /**
     * 支持SSL
     *
     * @return SSLConnectionSocketFactory
     */
    private static SSLConnectionSocketFactory getSslConnectionSocketFactory() throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        TrustStrategy acceptingTrustStrategy = (x509Certificates, s) -> true;
        SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
        return new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
    }
}
