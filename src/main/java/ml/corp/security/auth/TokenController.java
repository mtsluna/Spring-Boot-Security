package ml.corp.security.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RestController
public class TokenController {
	
	private UserAuthService authService;
	
	public TokenController(UserAuthService authService) {
		this.authService = authService;
	}

	@PostMapping("token")
	public Map<String, String> login(@RequestBody UserAuth userAuth) {
		
		String token = getJWTToken(userAuth);
		
		Map<String, String> response = new HashMap<String, String>();
		
		if(token.isEmpty()) {
			response.put("status", "Not authorized");
		}
		else {
			response.put("status", "Authorized");
			response.put("token", token);
		}
		
		return response;
		
	}
	
	private String getJWTToken(UserAuth userAuth) {
		
		if(authService.checkUser(userAuth)) {
			String secretKey = "mySecretKey";
			List<GrantedAuthority> grantedAuthorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList("ROLE_USER");
			
			String token = Jwts
					.builder()
					.setId("softtekJWT")
					.setSubject(userAuth.getUsername())
					.claim("authorities",
							grantedAuthorities.stream()
									.map(GrantedAuthority::getAuthority)
									.collect(Collectors.toList()))
					.setIssuedAt(new Date(System.currentTimeMillis()))
					.setExpiration(new Date(System.currentTimeMillis() + 600000))
					.signWith(SignatureAlgorithm.HS512,
							secretKey.getBytes()).compact();

			return "Bearer " + token;
		}
		
		return "";		
		
	}
	
}
