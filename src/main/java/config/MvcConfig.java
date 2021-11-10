package config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.web.servlet.config.annotation.DefaultServletHandlerConfigurer;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import controller.RegisterRequestValidator;
import interceptor.AuthCheckInterceptor;

@Configuration
@EnableWebMvc
public class MvcConfig implements WebMvcConfigurer {

	@Override
	public void configureDefaultServletHandling(DefaultServletHandlerConfigurer configurer) {
		configurer.enable();
	}

	//http://localhost:8080/sp5-chap13/ + /WEB-INF/view/ + * + .jsp 의 파일로 연결해줌
	//컨텍스트 + prefix + * + postfix
	@Override
	public void configureViewResolvers(ViewResolverRegistry registry) {
		registry.jsp("/WEB-INF/view/", ".jsp");
	}

	@Override
	public void addViewControllers(ViewControllerRegistry registry) { //단순 뷰 연결 처리
		registry.addViewController("/main").setViewName("main");
	}
	
	@Bean
	public MessageSource messageSource() { //뷰에서 사용할 메시지를 불러옴. 여러 언어를 쉽게 관리할 수 있음
		ResourceBundleMessageSource ms = new ResourceBundleMessageSource();
		ms.setBasenames("message.label");
		ms.setDefaultEncoding("UTF-8");
		return ms;
	}

	@Override
	public Validator getValidator() { //입력값에 문제가 없는지 확인
		return new RegisterRequestValidator();
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) { //edit 상위 view에 접근할 때 먼저 세션 정보를 확인
		registry.addInterceptor(authCheckInterceptor()).addPathPatterns("/edit/**");
	}
	
	@Bean
	public AuthCheckInterceptor authCheckInterceptor() {
		return new AuthCheckInterceptor();
	}
	
}
