package adlf.register_login.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration //Indicamos a Spring que esta clase tiene configuraciones necesarias
@EnableWebSecurity //Activamos Spring Security

public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthFilter, UserDetailsService userDetailsService,
                          PasswordEncoder passwordEncoder) {
        //Inyectamos
        this.jwtAuthFilter = jwtAuthFilter;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    //Antes de pasar a nuestro Bean principal, primero definimos un AuthenticationProvider necesario para el mismo
    @Bean
    public AuthenticationProvider authenticationProvider() {

        // DaoAuthenticationProvider es el proveedor estándar de Spring, autentica contra una bdd
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();

        // Le indicamos como debe buscar el usuario (en nuestro caso, mediante nuestro userDetailsService)
        provider.setUserDetailsService(userDetailsService);

        // Le indicamos como verificar passwords (con BCrypt)
        provider.setPasswordEncoder(passwordEncoder);

        return provider;
    }

    //Nuestro Bean principal, definimos reglas
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()) //Deshabilitamos csrf
                //CSRF es una proteccion de formularios HTML tradicionales
                //No lo necesitamos, pues con los tokens ya protegemos los requests

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/hello", "/api/auth/**").permitAll()
                        //Con permitAll, definimos que todas las request que especificamos son publicas
                        //Nuestro hello es publico, como tambien todos los que lleven /auth (Es decir, nuestra seccion login y register)
                        .anyRequest().authenticated()
                )
                //Cualquier otra request, debe llevar un token valido

                //No usamos sesiones
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                //Con STATELESS, indicamos que no existe estado en la sesion, ya con el token es suficiente para autenticar

                //Indicamos como autenticar usuarios, en este caso, con nuestro provider anteriormente declarado
                .authenticationProvider(authenticationProvider())
                //Nuestra metodologia es UserDetailsService y BCrypt

                //Agregamos nuestro filtro anteriormente hecho
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        //Colocamos antes del filtro nuestro jwtAuthFilter, para que primero ejecute el nuestro (valida el JWT)

        return http.build();
    }

    //Creamos el AuthenticationManager, necesario para el authService para verificar email + password cuando el usuario
    //logea. Spring lo genera automaticamente a partir de la configuracion, nosotros lo exponemos como Bean para
    //inyectarlo

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }


}
