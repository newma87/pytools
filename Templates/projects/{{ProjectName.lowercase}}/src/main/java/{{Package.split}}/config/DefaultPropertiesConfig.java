package {{Package}}.config;

import {{Package}}.config.property.*;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-01 19:55:06
 * Description:
 */
@Configuration
@EnableConfigurationProperties({ SecurityProperty.class })
public class DefaultPropertiesConfig {

    // 设置RestTemplate的超时时间
    @Bean
    public RestTemplate restTemplate() {
        /*HttpComponentsClientHttpRequestFactory httpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        httpRequestFactory.setConnectionRequestTimeout(10*1000);
        httpRequestFactory.setConnectTimeout(10*1000);
        httpRequestFactory.setReadTimeout(10*1000);*/
        // TODO: newma

        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters()
                .add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));

        return restTemplate;
    }
}
