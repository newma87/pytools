package {{Package}}.config;

import liquibase.configuration.GlobalConfiguration;
import liquibase.configuration.LiquibaseConfiguration;
import liquibase.integration.spring.SpringLiquibase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * Author: newma<newma@live.cn>
 * Create at: 2018-07-04 12:25:14
 * Description:
 */
@Configuration
@EnableJpaRepositories(basePackages = {"{{Package}}.repository"})
@EnableTransactionManagement
public class LiquibaseConfig {

    @Value("${default.liquibase.change-log}")
    private String changeLogPath;

    @Value("${default.liquibase.drop-first}")
    private boolean dropFirst;

    @Value("${default.liquibase.enabled}")
    private boolean enabled;

    @Autowired
    private DataSource dataSource;

    @Bean
    public SpringLiquibase liquibase() {
        GlobalConfiguration configuration = LiquibaseConfiguration.getInstance().getConfiguration(GlobalConfiguration.class);
        configuration.setDatabaseChangeLogTableName("LIQUIBASECHANGELOG");
        configuration.setDatabaseChangeLogLockTableName("LIQUIBASECHANGELOGLOCK");

        SpringLiquibase liquibase = new SpringLiquibase();
        liquibase.setDataSource(dataSource);
        liquibase.setShouldRun(enabled);
        liquibase.setDropFirst(dropFirst);
        liquibase.setChangeLog(changeLogPath);

        return liquibase;
    }
}
