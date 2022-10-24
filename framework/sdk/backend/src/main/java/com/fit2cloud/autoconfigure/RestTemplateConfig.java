package com.fit2cloud.autoconfigure;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fit2cloud.common.constants.RoleConstants;
import com.fit2cloud.common.utils.JwtTokenUtils;
import com.fit2cloud.dto.UserDto;
import com.fit2cloud.security.filter.JwtTokenAuthFilter;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.protocol.HttpContext;
import org.apache.http.ssl.SSLContexts;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpRequest;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestTemplate;

import javax.net.ssl.SSLContext;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @Author:张少虎
 * @Date: 2022/7/11  11:38 AM
 * @Version 1.0
 * @注释:
 */
public class RestTemplateConfig {

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate(generateHttpsRequestFactory());
        MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter = new MappingJackson2HttpMessageConverter();
        ObjectMapper mapper = new ObjectMapper();
        // 如果json中有新增的字段并且是实体类类中不存在的，不报错
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mappingJackson2HttpMessageConverter.setObjectMapper(mapper);
        restTemplate.getMessageConverters().add(0, mappingJackson2HttpMessageConverter);
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.getInterceptors().add(new TokenRequestInterceptor());
        return restTemplate;
    }

    /**
     * 获取客户端工厂
     *
     * @return httprest客户端工厂
     */
    public HttpComponentsClientHttpRequestFactory generateHttpsRequestFactory() {
        PoolingHttpClientConnectionManager connectionManager =
                new PoolingHttpClientConnectionManager(30, TimeUnit.SECONDS);
        connectionManager.setMaxTotal(200);
        connectionManager.setDefaultMaxPerRoute(20);

        try {
            TrustStrategy acceptingTrustStrategy = (x509Certificates, authType) -> true;
            SSLContext sslContext = SSLContexts.custom().loadTrustMaterial(null, acceptingTrustStrategy).build();
            SSLConnectionSocketFactory connectionSocketFactory = new SSLConnectionSocketFactory(sslContext, new NoopHostnameVerifier());
            ConnectionKeepAliveStrategy connectionKeepAliveStrategy = new ConnectionKeepAliveStrategy() {
                @Override
                public long getKeepAliveDuration(org.apache.http.HttpResponse httpResponse,
                                                 HttpContext httpContext) {
                    return 20 * 1000; // 20 seconds,because tomcat default keep-alive timeout is 20s
                }
            };
            HttpClientBuilder httpClientBuilder = HttpClients.custom().setKeepAliveStrategy(connectionKeepAliveStrategy).setConnectionManager(connectionManager).setRetryHandler(new DefaultHttpRequestRetryHandler());
            httpClientBuilder.setSSLSocketFactory(connectionSocketFactory);
            CloseableHttpClient httpClient = httpClientBuilder.build();
            HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
            factory.setBufferRequestBody(false);
            factory.setHttpClient(httpClient);
            factory.setConnectTimeout(60 * 1000);
            factory.setReadTimeout(60 * 1000);
            return factory;
        } catch (Exception e) {
            throw new RuntimeException("创建HttpsRestTemplate失败", e);
        }
    }

    /**
     * 请求前置处理器
     */
    public static class TokenRequestInterceptor implements ClientHttpRequestInterceptor {
        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {
            request.getHeaders().setAccept(List.of(MediaType.APPLICATION_JSON));
            if (SecurityContextHolder.getContext() != null && SecurityContextHolder.getContext().getAuthentication() != null && SecurityContextHolder.getContext().getAuthentication().getCredentials() != null) {
                UserDto userDto = (UserDto) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
                request.getHeaders().add(JwtTokenUtils.TOKEN_NAME, SecurityContextHolder.getContext().getAuthentication().getCredentials().toString());
                request.getHeaders().add(RoleConstants.ROLE_TOKEN, userDto.getCurrentRole().name());
                request.getHeaders().add(JwtTokenAuthFilter.CE_SOURCE_TOKEN, userDto.getCurrentSource());
            }
            return execution.execute(request, body);
        }
    }
}
