package cn.edu.nju.movietubeserver.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Tag;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author dc
 * @date 2019/12/20 18:05
 */
@Configuration
@EnableSwagger2
public class SwaggerConfig
{

    @Bean
    public Docket createRestApi()
    {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
            .tags(new Tag("user-tag", "用户模块的相关接口"))
            .tags(new Tag("movie-tag", "电影模块的相关接口"))
            .tags(new Tag("comment-tag", "评论模块的相关接口"))
            .tags(new Tag("rate-tag", "评分模块的相关接口"))
            .tags(new Tag("tag-tag", "电影标签模块的相关接口"))
            .tags(new Tag("admin-tag", "管理员的相关接口"))
            .tags(new Tag("chart-tag", "图表的相关接口"))
            .select()
            .apis(RequestHandlerSelectors.basePackage("cn.edu.nju.movietubeserver.controller"))
            .paths(PathSelectors.any())
            .build();
    }

    private ApiInfo apiInfo()
    {
        return new ApiInfoBuilder().title("Movie Tube Server 接口文档说明").description("后端接口文档说明").version("1.0").build();
    }
}
