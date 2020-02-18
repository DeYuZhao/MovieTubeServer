package cn.edu.nju.movietubeserver.config;

import cn.edu.nju.movietubeserver.support.jwt.JWTAuthenticationEntryPoint;
import cn.edu.nju.movietubeserver.support.jwt.JWTAuthenticationFilter;
import cn.edu.nju.movietubeserver.support.security.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * @author dc
 * @date 2019/12/22 15:20
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig extends WebSecurityConfigurerAdapter
{

    @Autowired
    private JWTAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Autowired
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    @Override
    public UserDetailsService userDetailsService()
    {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder()
    {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(final AuthenticationManagerBuilder auth)
        throws Exception
    {
        auth
            // 自定义获取用户信息
            .userDetailsService(userDetailsService())
            // 设置密码加密
            .passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(final HttpSecurity http)
        throws Exception
    {
        http.cors()
            .and()
            // 关闭csrf
            .csrf()
            .disable()
            // 无状态Session
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and()
            // 异常处理
            .exceptionHandling()
            .authenticationEntryPoint(jwtAuthenticationEntryPoint)
            .and()
            // 对所有的请求都做权限校验
            .authorizeRequests()
            // 允許访问静态资源
            .antMatchers("/",
                "/favicon.ico",
                "/**/*.ttf",
                "/**/*.png",
                "/**/*.gif",
                "/**/*.svg",
                "/**/*.jpg",
                "/**/*.html",
                "/**/*.css",
                "/**/*.css.map",
                "/**/*.js",
                "/**/*.js.map")
            .permitAll()

            // 允许登录和注册
            .antMatchers("/api/user/login", "/api/user/register", "/api/user/logout")
            .permitAll()

            // swagger
            .antMatchers("/api")
            .permitAll()
            .antMatchers("/v2/api-docs", "/configuration/**", "/swagger*/**", "/webjars/**")
            .permitAll()

            // 除上面外的所有请求全部需要鉴权认证
            .anyRequest()
            .authenticated()
            .and();

        http    // 基于定制JWT安全过滤器
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        // 禁用页面缓存
        http.headers().cacheControl();
    }
}
