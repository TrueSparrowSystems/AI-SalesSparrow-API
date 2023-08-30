package com.salessparrow.api.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.salessparrow.api.filter.SameSiteCookieFilter;

@Configuration
public class FilterConfig {

	/**
	 * Register SameSiteCookieFilter
	 * @return FilterRegistrationBean<SameSiteCookieFilter>
	 */
	@Bean
	public FilterRegistrationBean<SameSiteCookieFilter> sameSiteCookieFilter() {
		FilterRegistrationBean<SameSiteCookieFilter> registrationBean = new FilterRegistrationBean<>();
		registrationBean.setFilter(new SameSiteCookieFilter());
		registrationBean.addUrlPatterns("/*"); // or specific URL patterns
		return registrationBean;
	}

}
