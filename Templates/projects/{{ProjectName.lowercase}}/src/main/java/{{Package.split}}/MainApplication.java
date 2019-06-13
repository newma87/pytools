package {{Package}};

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.core.env.Environment;
import de.codecentric.boot.admin.server.config.EnableAdminServer;

import java.net.InetAddress;
import java.net.UnknownHostException;

@SpringBootApplication
@EnableAdminServer
public class MainApplication extends SpringBootServletInitializer {
	private static final Logger log = LoggerFactory.getLogger(MainApplication.class.getName());

	// 重载SpringBootServletInitializer.configure是为了打包成war包后能在tomcat中顺利运行
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder app) {
		return app.sources(MainApplication.class);
	}

	public static void main(String[] args) throws UnknownHostException {
		SpringApplication app = new SpringApplication(MainApplication.class);
		Environment env = app.run(args).getEnvironment();
		String protocol = "http";
		if (env.getProperty("server.ssl.key-store") != null) {
			protocol = "https";
		}
		log.info("\n----------------------------------------------------------\n\t" +
						"Application '{}' is running! Access URLs:\n\t" +
						"Local: \t\t{}://localhost:{}\n\t" +
						"External: \t{}://{}:{}\n\t" +
						"Profile(s): \t{}\n----------------------------------------------------------",
				env.getProperty("spring.application.name"),
				protocol,
				env.getProperty("server.port"),
				protocol,
				InetAddress.getLocalHost().getHostAddress(),
				env.getProperty("server.port"),
				env.getActiveProfiles());

		String configServerStatus = env.getProperty("configserver.status");
		log.info("\n-------------------------- spring cloud --------------------------------\n\t" +
						"Config Server: \t{}\n----------------------------------------------------------",
				configServerStatus == null ? "Not found or not setup for this application" : configServerStatus);
	}
}
