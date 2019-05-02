package com.example.spring.linkedin.oauth2.user;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;

import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LinkedInUser
		implements OAuth2User {

	@JsonAnySetter
	Map<String, Object> extraParameters = new HashMap<>();

	@JsonProperty("lastName")
	Map<String, Map<String, String>> lastName;

	@JsonProperty("firstName")
	Map<String, Map<String, String>> firstName;

	@JsonProperty("localizedLastName")
	String localizedLastName;

	@JsonProperty("localizedFirstName")
	String localizedFirstName;

	@Override
	@JsonIgnore
	public String getName() {
		return String.valueOf(extraParameters.get("id"));
	}

	@JsonIgnore
	public String getEmail() {
		return getName() + "@linkedin.com";
	}

	@Override
	@JsonIgnore
	public List<GrantedAuthority> getAuthorities() {
		return Arrays.asList(
				new OAuth2UserAuthority("USER", getAttributes()),
				new SimpleGrantedAuthority("USER"));
	}

	@Override
	@JsonIgnore
	public Map<String, Object> getAttributes() {
		Map<String, Object> attributes = new HashMap<>(this.getExtraParameters());
		attributes.put("firstName", firstName);
		attributes.put("lastName", lastName);
		attributes.put("localizedFirstName", localizedFirstName);
		attributes.put("localizedLastName", localizedLastName);

		return Collections.unmodifiableMap(attributes);
	}

	public String toString() {
		return getAttributes().toString();
	}

}