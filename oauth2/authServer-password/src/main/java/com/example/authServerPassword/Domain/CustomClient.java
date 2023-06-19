package com.example.authServerPassword.Domain;


import com.example.authServerPassword.constrant.ClientType;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.client.Jackson2ArrayOrStringDeserializer;
import org.springframework.util.StringUtils;

import java.util.*;

public class CustomClient implements ClientDetails {
	private static final long serialVersionUID = 5840531070411146325L; // 직렬화 버전의 고유값

	@JsonProperty("client_id")
	private String clientId;
	@JsonProperty("client_secret")
	private String clientSecret;
	@JsonDeserialize(
			using = Jackson2ArrayOrStringDeserializer.class
	)
	private Set<String> scope;

	@JsonProperty("authorized_grant_types")
	@JsonDeserialize(
			using = Jackson2ArrayOrStringDeserializer.class
	)
	private Set<String> authorizedGrantTypes;

	@JsonProperty("autoapprove")
	@JsonDeserialize(
			using = Jackson2ArrayOrStringDeserializer.class
	)
	private Set<String> autoApproveScopes;
	private List<GrantedAuthority> authorities;
	@JsonProperty("access_token_validity")
	private Integer accessTokenValiditySeconds;
	@JsonProperty("refresh_token_validity")
	private Integer refreshTokenValiditySeconds;
	@JsonIgnore
	private Map<String, Object> additionalInformation;

	public CustomClient() {
		this.scope = Collections.emptySet();
		this.authorizedGrantTypes = Collections.emptySet();
		this.authorities = Collections.emptyList();
		this.additionalInformation = new LinkedHashMap();
	}
	public CustomClient(ClientDetails prototype) {
		this();
		this.setAccessTokenValiditySeconds(prototype.getAccessTokenValiditySeconds());
		this.setRefreshTokenValiditySeconds(prototype.getRefreshTokenValiditySeconds());
		this.setAuthorities(prototype.getAuthorities());
		this.setAuthorizedGrantTypes(prototype.getAuthorizedGrantTypes());
		this.setClientId(prototype.getClientId());
		this.setClientSecret(prototype.getClientSecret());
		this.setScope(prototype.getScope());
	}

	public CustomClient(String clientId, String scopes, String grantTypes, String authorities) {
		//this(clientId, scopes, grantTypes, authorities);
		this.scope = Collections.emptySet();
		this.authorizedGrantTypes = Collections.emptySet();
		this.authorities = Collections.emptyList();
		this.additionalInformation = new LinkedHashMap();
		this.clientId = clientId;
		Set scopeList;

		if (StringUtils.hasText(scopes)) {
			scopeList = StringUtils.commaDelimitedListToSet(scopes);
			if (!scopeList.isEmpty()) {
				this.scope = scopeList;
			}
		}

		if (StringUtils.hasText(grantTypes)) {
			this.authorizedGrantTypes = StringUtils.commaDelimitedListToSet(grantTypes);
		} else {
			this.authorizedGrantTypes = new HashSet(Arrays.asList("authorization_code", "refresh_token"));
		}

		if (StringUtils.hasText(authorities)) {
			this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
		}
	}
//
//	public CustomClien1t(String clientId , String scopes, String grantTypes, String authorities) {
//		this.scope = Collections.emptySet();
//		this.authorizedGrantTypes = Collections.emptySet();
//		this.authorities = Collections.emptyList();
//		this.additionalInformation = new LinkedHashMap();
//		this.clientId = clientId;
//		Set scopeList;
//
//		if (StringUtils.hasText(scopes)) {
//			scopeList = StringUtils.commaDelimitedListToSet(scopes);
//			if (!scopeList.isEmpty()) {
//				this.scope = scopeList;
//			}
//		}
//
//		if (StringUtils.hasText(grantTypes)) {
//			this.authorizedGrantTypes = StringUtils.commaDelimitedListToSet(grantTypes);
//		} else {
//			this.authorizedGrantTypes = new HashSet(Arrays.asList("authorization_code", "refresh_token"));
//		}
//
//		if (StringUtils.hasText(authorities)) {
//			this.authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(authorities);
//		}
//	}



	@JsonIgnore
	public String getClientId() {
		return this.clientId;
	}

	@Override
	public Set<String> getResourceIds() {
		return null;
	}


	public void setClientId(String clientId) {
		this.clientId = clientId;
	}
	public void setAutoApproveScopes(Collection<String> autoApproveScopes) {
		this.autoApproveScopes = new HashSet(autoApproveScopes);
	}

	public boolean isAutoApprove(String scope) {
		if (this.autoApproveScopes == null) {
			return false;
		} else {
			Iterator var2 = this.autoApproveScopes.iterator();

			String auto;
			do {
				if (!var2.hasNext()) {
					return false;
				}

				auto = (String)var2.next();
			} while(!auto.equals("true") && !scope.matches(auto));

			return true;
		}
	}

	@JsonIgnore
	public Set<String> getAutoApproveScopes() {
		return this.autoApproveScopes;
	}

	@JsonIgnore
	public boolean isSecretRequired() {
		return this.clientSecret != null;
	}

	@JsonIgnore
	public String getClientSecret() {
		return this.clientSecret;
	}

	public void setClientSecret(String clientSecret) {
		this.clientSecret = clientSecret;
	}

	@JsonIgnore
	public boolean isScoped() {
		return this.scope != null && !this.scope.isEmpty();
	}

	public Set<String> getScope() {
		return this.scope;
	}

	public void setScope(Collection<String> scope) {
		this.scope = (Set)(scope == null ? Collections.emptySet() : new LinkedHashSet(scope));
	}


	@JsonIgnore
	public Set<String> getAuthorizedGrantTypes() {
		return this.authorizedGrantTypes;
	}

	@Override
	public Set<String> getRegisteredRedirectUri() {
		return null;
	}


	public void setAuthorizedGrantTypes(Collection<String> authorizedGrantTypes) {
		this.authorizedGrantTypes = new LinkedHashSet(authorizedGrantTypes);
	}


	@JsonProperty("authorities")
	private List<String> getAuthoritiesAsStrings() {
		return new ArrayList(AuthorityUtils.authorityListToSet(this.authorities));
	}

	@JsonProperty("authorities")
	@JsonDeserialize(
			using = Jackson2ArrayOrStringDeserializer.class
	)
	private void setAuthoritiesAsStrings(Set<String> values) {
		this.setAuthorities(AuthorityUtils.createAuthorityList((String[])values.toArray(new String[values.size()])));
	}

	@JsonIgnore
	public Collection<GrantedAuthority> getAuthorities() {
		return this.authorities;
	}

	@JsonIgnore
	public void setAuthorities(Collection<? extends GrantedAuthority> authorities) {
		this.authorities = new ArrayList(authorities);
	}

	@JsonIgnore
	public Integer getAccessTokenValiditySeconds() {
		return this.accessTokenValiditySeconds;
	}

	public void setAccessTokenValiditySeconds(Integer accessTokenValiditySeconds) {
		this.accessTokenValiditySeconds = accessTokenValiditySeconds;
	}

	@JsonIgnore
	public Integer getRefreshTokenValiditySeconds() {
		return this.refreshTokenValiditySeconds;
	}

	public void setRefreshTokenValiditySeconds(Integer refreshTokenValiditySeconds) {
		this.refreshTokenValiditySeconds = refreshTokenValiditySeconds;
	}

	public void setAdditionalInformation(Map<String, ?> additionalInformation) {
		this.additionalInformation = new LinkedHashMap(additionalInformation);
	}

	@JsonAnyGetter
	public Map<String, Object> getAdditionalInformation() {
		return Collections.unmodifiableMap(this.additionalInformation);
	}

	@JsonAnySetter
	public void addAdditionalInformation(String key, Object value) {
		this.additionalInformation.put(key, value);
	}

	public int hashCode() {
		int prime = 1;
		int result = 1;
		result = 31 * result + (this.accessTokenValiditySeconds == null ? 0 : this.accessTokenValiditySeconds);
		result = 31 * result + (this.refreshTokenValiditySeconds == null ? 0 : this.refreshTokenValiditySeconds);
		result = 31 * result + (this.authorities == null ? 0 : this.authorities.hashCode());
		result = 31 * result + (this.authorizedGrantTypes == null ? 0 : this.authorizedGrantTypes.hashCode());
		result = 31 * result + (this.clientId == null ? 0 : this.clientId.hashCode());
		result = 31 * result + (this.clientSecret == null ? 0 : this.clientSecret.hashCode());
		//result = 31 * result + (this.registeredRedirectUris == null ? 0 : this.registeredRedirectUris.hashCode());
	//	result = 31 * result + (this.resourceIds == null ? 0 : this.resourceIds.hashCode());
		result = 31 * result + (this.scope == null ? 0 : this.scope.hashCode());
		result = 31 * result + (this.additionalInformation == null ? 0 : this.additionalInformation.hashCode());
		return result;
	}


	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		} else if (obj == null) {
			return false;
		} else if (this.getClass() != obj.getClass()) {
			return false;
		} else {
			CustomClient other = (CustomClient)obj;
			if (this.accessTokenValiditySeconds == null) {
				if (other.accessTokenValiditySeconds != null) {
					return false;
				}
			} else if (!this.accessTokenValiditySeconds.equals(other.accessTokenValiditySeconds)) {
				return false;
			}

			if (this.refreshTokenValiditySeconds == null) {
				if (other.refreshTokenValiditySeconds != null) {
					return false;
				}
			} else if (!this.refreshTokenValiditySeconds.equals(other.refreshTokenValiditySeconds)) {
				return false;
			}

			if (this.authorities == null) {
				if (other.authorities != null) {
					return false;
				}
			} else if (!this.authorities.equals(other.authorities)) {
				return false;
			}

			if (this.authorizedGrantTypes == null) {
				if (other.authorizedGrantTypes != null) {
					return false;
				}
			} else if (!this.authorizedGrantTypes.equals(other.authorizedGrantTypes)) {
				return false;
			}

			if (this.clientId == null) {
				if (other.clientId != null) {
					return false;
				}
			} else if (!this.clientId.equals(other.clientId)) {
				return false;
			}

			if (this.clientSecret == null) {
				if (other.clientSecret != null) {
					return false;
				}
			} else if (!this.clientSecret.equals(other.clientSecret)) {
				return false;
			}

			if (this.scope == null) {
				if (other.scope != null) {
					return false;
				}
			} else if (!this.scope.equals(other.scope)) {
				return false;
			}

			if (this.additionalInformation == null) {
				if (other.additionalInformation != null) {
					return false;
				}
			} else if (!this.additionalInformation.equals(other.additionalInformation)) {
				return false;
			}
			return true;
		}
	}

	public String toString() {
		return "BaseClientDetails [clientId=" + this.clientId + ", clientSecret=" + this.clientSecret + ", scope=" + this.scope + ", authorizedGrantTypes=" + this.authorizedGrantTypes + ", authorities=" + this.authorities + ", accessTokenValiditySeconds=" + this.accessTokenValiditySeconds + ", refreshTokenValiditySeconds=" + this.refreshTokenValiditySeconds + ", additionalInformation=" + this.additionalInformation + "]";
	}

}
