package com.salessparrow.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.XXssProtectionHeaderWriter;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter.ReferrerPolicy;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

		http
			// disable authorization for all routes
			.authorizeHttpRequests(authorizeRequests -> authorizeRequests.anyRequest().permitAll())
			// Disable authentication for all routes
			.httpBasic(httpBasic -> httpBasic.disable())
			// Disable form login
			.formLogin(formLogin -> formLogin.disable())
			// Remove csrf in app routes
			.csrf((csrf) -> csrf.ignoringRequestMatchers("/api/v1/**"))

			/**
			 * Cache-Control header - applied by default in spring security The
			 * Cache-Control header is the most important header to set as it effectively
			 * disables caching on the client side.
			 *
			 * Pragma header - applied by default in spring security The Pragma directive
			 * is an older directive meant for HTTP/1.0 clients where the Cache-Control
			 * header wasn't defined.
			 *
			 * Expires header - applied by default in spring security The Expires header
			 * is another older way to prevent caching, especially in HTTP/1.0.
			 *
			 * X-Content-Type-Options header - applied by default in spring security The
			 * X-Content-Type-Options header is a security feature that prevents pages
			 * from loading when they detect incorrect MIME types. By setting the value to
			 * "nosniff", you're instructing the browser not to override the provided
			 * Content-Type
			 *
			 * HSTS header The Strict-Transport-Security header is a security feature
			 * implemented by web browsers to ensure that websites are only accessed using
			 * HTTPS.
			 *
			 * X-Frame-Options header The X-Frame-Options header is a security feature
			 * that prevents your web page from being put in a frame.
			 *
			 * X-XSS-Protection header - applied by default in spring security The
			 * X-XSS-Protection header is a security feature that prevents pages from
			 * loading when they detect reflected cross-site scripting (XSS) attacks.
			 *
			 * It is not needed for rest api's as they are not rendered in the browser.
			 * But it is no harm and is a good practice to have it.The value "1;
			 * mode=block" instructs the browser to block the response if it detects an
			 * attack.
			 *
			 * Referrer-Policy header The Referrer-Policy header is a security feature
			 * that prevents pages from leaking information about the user's browsing
			 * behavior.The value "same-origin" instructs the browser to send the referrer
			 * header only when the request is originating from the same origin as the
			 * target resource.
			 */
			.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.deny())
				.httpStrictTransportSecurity(
						hsts -> hsts.includeSubDomains(true).preload(true).maxAgeInSeconds(31536000))
				.xssProtection(xss -> xss.headerValue(XXssProtectionHeaderWriter.HeaderValue.ENABLED_MODE_BLOCK))
				.referrerPolicy(referrer -> referrer.policy(ReferrerPolicy.SAME_ORIGIN)));

		// Enable for production and staging
		if (!CoreConstants.isDevEnvironment() && !CoreConstants.isTestEnvironment()
				&& !CoreConstants.isLocalTestEnvironment()) {
			// All requests must be secure
			http.requiresChannel(channel -> channel.anyRequest().requiresSecure());
		}

		return http.build();
	}

}
