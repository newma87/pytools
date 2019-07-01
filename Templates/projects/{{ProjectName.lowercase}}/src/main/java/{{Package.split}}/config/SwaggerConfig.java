package {{Package}}.config;

import {{Package}}.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**
 * Author: huangpeilin
 * Create at: 2018-05-29 13:20:16
 * Description:
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket createRestApi(TokenProvider tokenProvider) {
        List<String> authorities = new ArrayList<>();
        authorities.add("ROLE_ADMIN");
        String token = tokenProvider.createToken("admin",  authorities, true);
        ParameterBuilder tokenPar = new ParameterBuilder();
        List<Parameter> pars = new ArrayList<Parameter>();
        tokenPar.name("Authorization")
                .description("令牌, Bearer 开头")
                .modelRef(new ModelRef("string"))
                .defaultValue("Bearer " + token)
                .parameterType("header")
                .required(false)
                .build();
        pars.add(tokenPar.build());
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(RequestHandlerSelectors.basePackage("{{Package}}.controllers"))
                .paths(PathSelectors.any())
                .build()
                .globalOperationParameters(pars)
                .apiInfo(apiInfo());
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("Swagger REST API")
                .description("无纸化会议系统后台接口")
                .termsOfServiceUrl("")
                .version("1.0")
                .build();
    }
}
