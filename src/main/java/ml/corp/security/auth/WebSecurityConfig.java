package ml.corp.security.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.bind.annotation.CrossOrigin;

/*
 *  Esta clase habilita la seguridad
 *  y esta definida como una clase de 
 *  configuración.
 */
@CrossOrigin(origins = "*")
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	/*
	 * Metodo de configuración
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors();
		//Se deshabilita la seguridad contra la falsificación de credenciales en origenes cruzados
		http.csrf().disable()
			//Se le agrega un nuevo filtro del token a la seguridad, y el metodo de auth
			.addFilterAfter(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)
			//Se define que las request deben de ser authorizadas
			.authorizeRequests()
			/*
			 * 	Y habilita unicamenta las peticiones hacia las URL y metodos HTTP que terminen con un
			 *	.permitAll()
			*/
			.antMatchers(HttpMethod.POST, "/api/auth/new/token").permitAll()
			.antMatchers(HttpMethod.POST, "/api/auth/refresh/token").permitAll()
			.antMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
			// Y determina que toda peticion que no sea la anterior debe de estar autentificada.
			.anyRequest().authenticated();
		
	}
	
}