package {{Package}}.filter;

import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-11-29 10:17:45
 * Description:
 */
public class RequestLoggingFilter extends CommonsRequestLoggingFilter {

    protected List<AntPathRequestMatcher> disallowUrls = new ArrayList<>();
    private HttpMethod requestMethod;

    public RequestLoggingFilter() {
        super();

        setIncludeQueryString(true);
        setIncludePayload(true);
        setIncludeClientInfo(true);
        setMaxPayloadLength(1024);
        setIncludeHeaders(false);
        setAfterMessagePrefix("REQUEST DATA : ");
    }

    public RequestLoggingFilter get() {
        requestMethod = HttpMethod.GET;
        return this;
    }

    public RequestLoggingFilter post() {
        requestMethod = HttpMethod.POST;
        return this;
    }

    public RequestLoggingFilter exclude(String requestPattern) {
        disallowUrls.add(new AntPathRequestMatcher(requestPattern, requestMethod.toString()));
        return this;
    }

    @Override
    protected boolean shouldLog(HttpServletRequest request) {
        for (AntPathRequestMatcher matcher : disallowUrls) {
            if (matcher.matches(request)) {
                return false;
            }
        }

        return true;
    }
}
