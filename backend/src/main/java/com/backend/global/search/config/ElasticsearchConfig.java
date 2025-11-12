package com.backend.global.search.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticsearchConfig extends ElasticsearchConfiguration {

    @Value("${spring.elasticsearch.uris}")
    private String elasticsearchUrl;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                // URI에서 "http://"를 제거하지 마세요
                // Elasticsearch가 실제 포트를 포함한 주소를 필요로 합니다.
                .connectedTo(elasticsearchUrl.replace("http://", "")) // localhost:9200 형태로 변환
                .build();
    }
}
