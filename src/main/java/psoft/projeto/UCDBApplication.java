package psoft.projeto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

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
		FilterRegistrationBean<TokenFilter> filter = new FilterRegistrationBean<TokenFilter>();
		filter.setFilter(new TokenFilter());
		filter.addUrlPatterns(CourseController.getPrivatePatterns());
		return filter;
	}

}
