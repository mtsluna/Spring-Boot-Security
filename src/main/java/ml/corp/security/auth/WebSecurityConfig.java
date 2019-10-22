package ml.corp.security.auth;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/*
 *  Esta clase habilita la seguridad
 *  y esta definida como una clase de 
 *  configuración.
 */
@EnableWebSecurity
@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter{

	/*
	 * Metodo de configuración
	*/
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
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
			.antMatchers(HttpMethod.POST, "/token").permitAll()
			// Y determina que toda peticion que no sea la anterior debe de estar autentificada.
			.anyRequest().authenticated();
		
	}
	
}