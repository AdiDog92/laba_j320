package src.ui_module.rest_client;

import java.net.http.HttpClient;
import java.time.Duration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.JdkClientHttpRequestFactory;
import org.springframework.web.client.RestClient;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class BackServiceConfig {

    private final String baseUrl;

    private final Duration connectTimeout;

    public BackServiceConfig(
			@Value("${backend-module.base-url}") String baseUrl,
			@Value("${backend-module.timeout}") Duration connectTimeout
    ) {
        this.baseUrl = baseUrl;
        this.connectTimeout = connectTimeout;
    }

    @Bean
    public RestClient externalRestClient() {
        var httpClient = HttpClient.newBuilder()
                .connectTimeout(connectTimeout)
                .build();

        return RestClient.builder()
                .baseUrl(baseUrl)
                .requestFactory(new JdkClientHttpRequestFactory(httpClient))
                .defaultHeader("Accept", "application/json")
                .build();
    }
}
