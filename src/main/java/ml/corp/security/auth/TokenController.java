package ml.corp.security.auth;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

	//Endpoint de logeo
	@PostMapping("token")
	public ResponseEntity login(@RequestBody UserAuth userAuth) {
		
		//Se recibe el token generado mediante el metodo
		String token = getJWTToken(userAuth);
		
		//Se define un mapa para la respuesta
		Map<String, String> response = new HashMap<String, String>();
		HttpStatus status = HttpStatus.OK;
		
		//Si el token esta vacio
		if(token.isEmpty()) {
			//Se define como no autorizado
			response.put("status", "Not authorized");
			status = HttpStatus.UNAUTHORIZED;
		}
		else {
			//Se define el status como autorizado
			response.put("status", "Authorized");
			//Se retorna el token en la respuesta
			response.put("token", token);			
		}
		
		return ResponseEntity.status(status).body(response);
		
	}
	
	private String getJWTToken(UserAuth userAuth) {
		
		//Si se determina que el usuario y password son correctos
		if(authService.checkUser(userAuth)) {
			//Se define la clase secreta
			String secretKey = "mySecretKey";
			//Se define una lista de autoridades a la hora de crear el token
			List<GrantedAuthority> grantedAuthorities = AuthorityUtils
					.commaSeparatedStringToAuthorityList("ROLE_USER");
			
			//Se define el token
			String token = Jwts
					//Se lo construye
					.builder()
					//Se le setea un ID, el cual puede ser cualquiera
					.setId("mluna")
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

			//Se retorna el token concatenado con la palabra Bearer al comienzo.
			return "Bearer " + token;
		}
		
		//En el caso de que el usuario que se envio en el body no exista se retorna un token vacio.
		return "";		
		
	}
	
}
