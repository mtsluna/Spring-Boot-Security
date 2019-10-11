package ml.corp.security.auth;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Base64;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

@Entity(name = "user_auth")
public class UserAuth {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_auth_id")
	private int id;
	
	@Transient
	private String password;
	
	@Column(name = "user_auth_password")
	private String encriptedPassword;
	
	@Column(name = "user_auth_username", unique = true)
	private String username;
	
	@Transient
	private String token;

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
				
		this.encriptedPassword = Base64.getEncoder().encodeToString(password.getBytes());
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			byte[] hashInBytes = md.digest(this.encriptedPassword.getBytes(StandardCharsets.UTF_8));
			
			StringBuilder sb = new StringBuilder();
			for(byte b : hashInBytes) {
				sb.append(String.format("%02x", b));
			}
			
			this.encriptedPassword = sb.toString();
			
		} catch (Exception e) {}
		
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		
		username = Base64.getEncoder().encodeToString(username.getBytes());
		
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			
			byte[] hashInBytes = md.digest(username.getBytes(StandardCharsets.UTF_8));
			
			StringBuilder sb = new StringBuilder();
			for(byte b : hashInBytes) {
				sb.append(String.format("%02x", b));
			}
			
			username = sb.toString();
			
		} catch (Exception e) {}
		
		this.username = username;
		
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
}
