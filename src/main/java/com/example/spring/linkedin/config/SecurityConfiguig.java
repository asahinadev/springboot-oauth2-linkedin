package com.example.spring.linkedin.config;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.example.spring.linkedin.oauth2.CustomOAuth2AccessTokenResponseClient;

@Configuration
@EnableWebSecurity
public class SecurityConfiguig
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
				.and()

		;

	}

	private CustomOAuth2AccessTokenResponseClient accessTokenResponseClient() {

		RestTemplate template = new RestTemplate(Arrays.asList(
				new FormHttpMessageConverter(),
				new MappingJackson2HttpMessageConverter(),
				new OAuth2AccessTokenResponseHttpMessageConverter()));

		template.setErrorHandler(new OAuth2ErrorResponseErrorHandler());

		return new CustomOAuth2AccessTokenResponseClient(template);
	}

	<T> List<T> $(@SuppressWarnings("unchecked") T... ts) {
		return Arrays.asList(ts);
	}
}
