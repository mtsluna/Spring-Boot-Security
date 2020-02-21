package ml.corp.security.controllers;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.persistence.MappedSuperclass;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import ml.corp.security.auth.JWTAuthorizationFilter;
import ml.corp.security.models.Token;
import ml.corp.security.models.UserAuth;
import ml.corp.security.services.UserAuthService;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path = "api/auth")
public class TokenController {
	
	private UserAuthService authService;
	
	@Autowired
	private JWTAuthorizationFilter jwtAuthorizationFilter;
	
	public TokenController(UserAuthService authService) {
		this.authService = authService;
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("register")
	public ResponseEntity register(@RequestBody UserAuth userAuth) {
		if(this.authService.registerUser(userAuth)) {
			return ResponseEntity.status(HttpStatus.CREATED).body("");
		}
		else {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("");
		}
	}
	
	//Endpoint de logeo
	@CrossOrigin(origins = "*")
	@PostMapping("new/token")
	public ResponseEntity<Token> login(@RequestBody UserAuth userAuth) {
		
		if(authService.checkUser(userAuth)) {
			return getTokenResponse(userAuth);
		}
		else {
			return getTokenFailResponse("Invalid credentials.");
		}
		
	}
	
	@CrossOrigin(origins = "*")
	@PostMapping("refresh/token")
	public ResponseEntity<Token> refreshLogin(@RequestBody Token token) {
		
		String refreshToken = token.getRefreshToken();
		
		try {
			
			if (refreshToken != null) {
				Claims claims = Jwts.parser().setSigningKey(jwtAuthorizationFilter.getSecretRefreshToken().getBytes()).parseClaimsJws(refreshToken).getBody();
				if (claims.get("authorities") != null && claims.get("scope").equals("refresh")) {
					UserAuth userAuth = new UserAuth();
					userAuth.setUsername(claims.get("username").toString());
					userAuth.setPassword("ASD");
					return getTokenResponse(userAuth);				
				} else {
					return getTokenFailResponse("Refresh token not valid.");
				}
			}	
			
		} catch (Exception e) {
			return getTokenFailResponse("Refresh token expired.");
		}
		
		return null;		
				
	}
	
	public ResponseEntity<Token> getTokenFailResponse(String reason) {
		Token token = new Token();
		token.setStatus("Not authorized.");
		token.setReason(reason);
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(token);
	}
	
	public ResponseEntity<Token> getTokenResponse(UserAuth userAuth) {
		String [] resp = getJWTToken(userAuth);
		
		String accessToken = resp != null ? resp[0] : "";
		String refreshToken = resp != null ? resp[1] : "";
		
		//Se define un mapa para la respuesta
		HttpStatus status = HttpStatus.OK;
		
		Token token = new Token();
		
		//Si el token esta vacio
		if(accessToken.isEmpty()) {
			return getTokenFailResponse("Error to create token.");
		}
		else {
			//Se define el status como autorizado
			token.setStatus("Authorized");
			token.setAccessToken(accessToken);
			token.setRefreshToken(refreshToken);
			token.setExpiresOn(1800L);			
		}
		return ResponseEntity.status(status).body(token);
	}
	
	private String [] getJWTToken(UserAuth userAuth) {
		
		//Se define la clase secreta
		String secretKey = "mySecretKey";
		String refreshSecretKey = "TheSeCreTki";
		//Se define una lista de autoridades a la hora de crear el token
		List<GrantedAuthority> grantedAuthorities = AuthorityUtils
				.commaSeparatedStringToAuthorityList("ROLE_USER");
		
		//Se define el token
		String token = Jwts
				//Se lo construye
				.builder()
				//Se le setea un ID, el cual puede ser cualquiera
				.setId(new UUID(12, 13).toString())
				//Se le setea el usuario del sujeto
				.setSubject(userAuth.getUsername())
				//Se le setean los privilegios
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				//Se le setea la fecha de creación del token
				.setIssuedAt(new Date(System.currentTimeMillis()))
				//Se le setea su tiempo de expiración
				.setExpiration(new Date(System.currentTimeMillis() + 180000))
				//Se firma el token y se lo encripta con la clave secreta.
				.signWith(SignatureAlgorithm.HS512,
						secretKey.getBytes()).compact();
		token = "Bearer " + token;

		String refreshToken = Jwts
				//Se lo construye
				.builder()
				//Se le setea un ID, el cual puede ser cualquiera
				.setId(new UUID(12, 13).toString())
				//Se le setea el usuario del sujeto
				.setSubject(userAuth.getUsername())
				//Se le setean los privilegios
				.claim("authorities",
						grantedAuthorities.stream()
								.map(GrantedAuthority::getAuthority)
								.collect(Collectors.toList()))
				.claim("scope", "refresh")
				.claim("username", userAuth.getUsername())
				//Se le setea la fecha de creación del token
				.setIssuedAt(new Date(System.currentTimeMillis()))
				//Se le setea su tiempo de expiración
				.setExpiration(new Date(System.currentTimeMillis() + 720000))
				//Se firma el token y se lo encripta con la clave secreta.
				.signWith(SignatureAlgorithm.HS512,
						refreshSecretKey.getBytes()).compact();
		
		//Se retorna el token concatenado con la palabra Bearer al comienzo.
		String [] resp = {token, refreshToken};
		return resp;	
		
	}
	
}
