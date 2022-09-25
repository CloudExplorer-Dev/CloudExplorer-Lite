package com.fit2cloud.common.es;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.ElasticsearchTransport;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fit2cloud.common.log.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.nio.client.HttpAsyncClientBuilder;
import org.elasticsearch.client.Node;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author jianneng
 * @date 2022/9/22 17:36
 **/
@Configuration
public class ElasticSearchConfig {
    @Resource
    private ElasticSearchProperty elasticSearchProperty;

    @Bean
    public RestClientBuilder restClientBuilder() {
        HttpHost[] httpHosts = this.getElasticSearchHttpHosts();
        return RestClient.builder(httpHosts).setRequestConfigCallback(requestConfigBuilder -> {
            //设置连接超时时间
            requestConfigBuilder.setConnectTimeout(elasticSearchProperty.getConnectTimeout());
            requestConfigBuilder.setSocketTimeout(elasticSearchProperty.getSocketTimeout());
            requestConfigBuilder.setConnectionRequestTimeout(elasticSearchProperty.getConnectionRequestTimeout());
            return requestConfigBuilder;
        }).setFailureListener(new RestClient.FailureListener() {
            //某节点失败,这里可以做一些告警
            @Override
            public void onFailure(Node node) {
                LogUtil.error("[ ElasticSearchClient ] >>  node :{}, host:{},  fail ", node.getName(), node.getHost());
            }
        }).setHttpClientConfigCallback(httpClientBuilder -> {
            httpClientBuilder.disableAuthCaching();
            //设置账密
            return getHttpAsyncClientBuilder(httpClientBuilder);
        });
    }

    /**
     * 配置es client
     */
    @Bean(value = "elasticsearchClient")
    public ElasticsearchClient elasticsearchClient(@Qualifier("restClientBuilder") RestClientBuilder restClientBuilder) {
        RestClient restClient = restClientBuilder.build();
        ElasticsearchTransport transport = new RestClientTransport(
                restClient, new JacksonJsonpMapper());
        return new ElasticsearchClient(transport);
    }

    /**
     * 连接地址,多个逗号分隔
     * 10.1.11.231:9200,10.1.11.232:9200
     */
    private HttpHost[] getElasticSearchHttpHosts() {
        String[] hosts = elasticSearchProperty.getAddress().split(",");
        HttpHost[] httpHosts = new HttpHost[hosts.length];
        for (int i = 0; i < httpHosts.length; i++) {
            String host = hosts[i];
            host = host.replaceAll("http://", "").replaceAll("https://", "");
            httpHosts[i] = new HttpHost(host.split(":")[0], Integer.parseInt(host.split(":")[1]), "http");
        }
        return httpHosts;
    }

    private HttpAsyncClientBuilder getHttpAsyncClientBuilder(HttpAsyncClientBuilder httpClientBuilder) {
        if (StringUtils.isEmpty(elasticSearchProperty.getUsername()) || StringUtils.isEmpty(elasticSearchProperty.getPasswd())) {
            return httpClientBuilder;
        }
        //设置账号密码
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(elasticSearchProperty.getUsername(), elasticSearchProperty.getPasswd()));
        httpClientBuilder.setDefaultCredentialsProvider(credentialsProvider);
        return httpClientBuilder;
    }
}
