package {{Package}}.common.auditor;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-06-05 19:02:48
 * Description:
 * @author newma
 */
public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("admin");
    }
}
