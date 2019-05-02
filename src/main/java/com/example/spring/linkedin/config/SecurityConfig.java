package com.example.spring.linkedin.config;

import java.util.Arrays;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.endpoint.DefaultAuthorizationCodeTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AccessTokenResponseClient;
import org.springframework.security.oauth2.client.endpoint.OAuth2AuthorizationCodeGrantRequest;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import com.example.spring.linkedin.oauth2.user.LinkedInUser;
import com.example.spring.oauth2.CustomOAuth2AccessTokenResponseHttpMessageConverter;
import com.example.spring.oauth2.LoggingClientHttpRequestInterceptor;

@Configuration
@EnableWebSecurity
public class SecurityConfig
		extends WebSecurityConfigurerAdapter {

	@Override
	public void configure(WebSecurity web)
			throws Exception {
		super.configure(web);

		web.ignoring().antMatchers(
				// webjars
				"/webjars/**",
				// CSS ファイル
				"/css/**",
				// JavaScriptファイル
				"/js/**",
				// 画像ファイル
				"/img/**",
				// サウンドファイル
				"/sound/**",
				// WEB フォント
				"/font/**",
				"/fonts/**",
				// 外部ライブラリ
				"/exlib/**"
		/**/
		);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
		super.configure(auth);
	}

	@Override
	protected void configure(HttpSecurity http)
			throws Exception {
		super.configure(http);

		http.formLogin().disable();
		http.logout().disable();

		http.httpBasic().disable();

		http.csrf().disable();

		http.oauth2Login()

				// 認証エンドポイント
				.authorizationEndpoint()
				.and()

				// トークン取得エンドポイント
				.tokenEndpoint()
				.accessTokenResponseClient(accessTokenResponseClient())
				.and()

				// リダイレクトエンドポイント
				.redirectionEndpoint()
				.and()

				// ユーザー情報エンドポイント
				.userInfoEndpoint()
				.customUserType(LinkedInUser.class, "linkedin")
				.and()

		;

	}

	OAuth2AccessTokenResponseClient<OAuth2AuthorizationCodeGrantRequest> accessTokenResponseClient() {
		DefaultAuthorizationCodeTokenResponseClient client = new DefaultAuthorizationCodeTokenResponseClient();

		RestTemplate restTemplate = new RestTemplate(Arrays.asList(
				new FormHttpMessageConverter(),
				new CustomOAuth2AccessTokenResponseHttpMessageConverter()));

		restTemplate.setErrorHandler(new OAuth2ErrorResponseErrorHandler());
		restTemplate.setInterceptors(Arrays.asList(new LoggingClientHttpRequestInterceptor()));

		client.setRestOperations(restTemplate);

		return client;

	}
}
