package psoft.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import psoft.projeto.controller.CourseController;
import psoft.projeto.controller.TokenFilter;

@SpringBootApplication
@ComponentScan({"psoft.projeto.controller", "psoft.projeto.service", "psoft.projeto.dao", "psoft.projeto.exception", "psoft.projeto.model"})
public class UCDBApplication {

	public static void main(String[] args) {
		SpringApplication.run(UCDBApplication.class, args);
	}
	
	@Bean
	public FilterRegistrationBean<TokenFilter> filterJwt() {
		FilterRegistrationBean<TokenFilter> filter = new FilterRegistrationBean<TokenFilter>(new TokenFilter());
		filter.addUrlPatterns(CourseController.getPrivatePatterns());
		return filter;
	}
	
	@Bean
	public FilterRegistrationBean corsFilter2() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		CorsConfiguration config = new CorsConfiguration().applyPermitDefaultValues();
		config.addAllowedMethod("*");
		source.registerCorsConfiguration("/**", config);
		FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
		bean.setOrder(0);
		return bean;
	}
}
