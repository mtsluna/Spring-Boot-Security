package ml.corp.security.auth;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.PrematureJwtException;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;

public class JWTAuthorizationFilter extends OncePerRequestFilter{

	/*
	 * 
	 * En toda petición con seguridad deben enviarse mediante
	 * el HEADER un conjunto clave valor, el cual debe de ser:
	 * 
	 * "Authorization": "Bearer eyJhbGciOiJIUzUxMiJ9.eyJqdGkiO
	 * iJzb2Z0dGVrSldUIiwic3ViIjoiNWZhNjJhZTYxNzZmMzc0NjE0MjUw
	 * M2E2ZWJlOTZjYjMiLCJhdXRob3JpdGllcyI6WyJST0xFX1VTRVIiXSw
	 * iaWF0IjoxNTcxMjMxNzIyLCJleHAiOjE1NzEyMzE5MDJ9.IFbpjrT8Y
	 * L-HjDwHu6bnmkv_9DPykaR6qnhWF31BAO_ZUPDhcJTw49wVUODXD8jl
	 * wwFEVUEUKSaAfzbmZqoFQA"
	 * 
	 */	
	private final String HEADER = "Authorization";
	private final String PREFIX = "Bearer ";
	private static final String SECRET = "mySecretKey";
	private static final String SECRET_REFRESH_TOKEN = "TheSeCreTki";

	/*
	 *
	 * Metodo que esta siendo redefinido.
	 * Recibe como paramentros la petición, la respuesta y
	 * un filtro.
	 * 
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
		//Intenta
		try {
			//Se checkea si se envio el token.
			if (checkJWTToken(request, response)) {
				// Se recibe el claim o los privilegios contenidos luego de validar el token.
				Claims claims = validateToken(request);
				//Si el token posee privilegio
				if (claims.get("authorities") != null) {
					//Se pasa a setear la apertura de la seguridad.
					setUpSpringAuthentication(claims);
				} else {
					//Si no se borra el contexto de la petición.
					SecurityContextHolder.clearContext();
				}
			}
			chain.doFilter(request, response);
		// En caso de arrojarse algun error se recibe un 403 de acceso denegado como respuesta.
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | PrematureJwtException | SignatureException e) {
			response.setStatus(HttpServletResponse.SC_FORBIDDEN);
			((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, e.getMessage());
			return;
		}
	}	
	
//	private boolean refreshToken() {
//		try {
//			
//			if ()
//			
//		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException e) {
//			
//		}
//	}

	/*
	 *  Metodo validador de tokens
	 */
	private Claims validateToken(HttpServletRequest request) {
		/* 
		 * Se extrae el header Authorization, se le quita la palabra
		 * Bearer al token.
		*/
		String jwtToken = request.getHeader(HEADER).replace(PREFIX, "");
		/*
		 * Se desencripta el token con la clave secreta.
		 */
		return Jwts.parser().setSigningKey(SECRET.getBytes()).parseClaimsJws(jwtToken).getBody();
	}

	/*
	 *
	 *  Si está todo OK, añade la configuración necesaria al contexto
	 *  de Spring para autorizar la petición.
	 * 
	 */
	private void setUpSpringAuthentication(Claims claims) {
		@SuppressWarnings("unchecked")
		//Rellena una lista con los privilegios
		List<String> authorities = (List<String>) claims.get("authorities");

		/*  Como la auth que utilizamos se maneja mediante nombre y usuario,
		 *  dentro de este token, manejamos los privilegios del usuario en
		 *  cuestión.
		 *  Por ultimo se la agrega al flujo de seguridad.
		*/
		UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(claims.getSubject(), null,
				authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList()));
		SecurityContextHolder.getContext().setAuthentication(auth);

	}

	/*
	 *  Funcion que verifica si se envio un token en la petición.
	 */
	private boolean checkJWTToken(HttpServletRequest request, HttpServletResponse res) {
		/*
		 *  Se verifica que exista el HEADER
		 */
		String authenticationHeader = request.getHeader(HEADER);
		/*
		 *  Si el HEADER de Authorization esta vacio o no tiene su
		 *  primera parte indispensable devuelve un FALSE, en caso de estar
		 *  correcto devuelve un TRUE.
		 */
		if (authenticationHeader == null || !authenticationHeader.startsWith(PREFIX))
			return false;
		return true;
	}

	public static String getSecret() {
		return SECRET;
	}

	public static String getSecretRefreshToken() {
		return SECRET_REFRESH_TOKEN;
	}
	
}
