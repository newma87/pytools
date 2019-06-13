package {{Package}}.config;

import {{Package}}.filter.RequestLoggingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

/**
 * Author: huangpeilin
 * Create at: 2018-07-09 16:53:59
 * Description:
 */
@Configuration
public class RequestLoggingFilterConfig {
    @Bean
    public CommonsRequestLoggingFilter logFilter() {
        CommonsRequestLoggingFilter filter = new RequestLoggingFilter();
        /*((RequestLoggingFilter) filter).get()
                .exclude("/apiv1/pad/apk/getTheLatest")
                .exclude("/apiv1/pad/user/activeClient");*/
        return filter;
    }
}
