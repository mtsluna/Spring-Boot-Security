package ml.corp.security.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonInclude(value = Include.NON_NULL)
public class Token {

	private String status;
	private String reason;
	@JsonProperty(value = "access_token")
	private String accessToken;
	@JsonProperty(value = "refresh_token")
	private String refreshToken;
	@JsonProperty(value = "expires_on")
	private Long expiresOn;
	
	public Token() {}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Long getExpiresOn() {
		return expiresOn;
	}

	public void setExpiresOn(Long expiresOn) {
		this.expiresOn = expiresOn;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}
		
}
