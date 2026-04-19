package adlf.register_login.api.config;

import adlf.register_login.api.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component //Spring lo registra como un bean (Validation)
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    //OncePerRequestFilter garantiza que el filtro se ejecute SOLO UNA VEZ por request (Sin tomar en cuenta redirects)

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        // Hacemos inyeccion de nuestros service, pues los requeriremos
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, //Nuestro request
                                    HttpServletResponse response, //La respuesta que vamos a mandar
                                    FilterChain filterChain) //La cadena de filtros (hay más aparte de este
            throws ServletException, IOException {

        //Buscamos el token en el header del request (El cliente envía: Header: Authorization: Bearer eyJhbGci...
        String authHeader = request.getHeader("Authorization");
        //Extraemos header.

        // SI NO HAY TOKEN, dejamos pasar el request tal cual
        // No cortamos acá, el SecurityConfig decide si la ruta necesita o no autenticación
        // Es decir, si la ruta es pública (como un /login) no pasa nada, en cambio si es privada
        // (Es decir, accedemos a un contenido privado como /usuarios/listas) tira error 403

        if (authHeader == null || !authHeader.startsWith("Bearer ")) { //Nuestro header que ejemplificamos arriba
            filterChain.doFilter(request, response);
            //.DoFilter pasa al siguiente filtro
            return; //Salimos del método, pues no hay Authorization
        }

        //Si no salimos (es decir, if de arriba tiró false = tenemos authorization), extraemos el token
        String token = authHeader.substring(7); //Arrancamos desde el final de "Bearer "
        //Extraemos su email para verificar si es valido
        String email = jwtService.extractEmail(token);
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            //El SecurityContextHolder... == null verifica que el usuario NO ESTE AUTENTICADO en este request
            //En caso contrario, si ya esta autenticado, no necesitamos hacer nada

            //Buscamos usuario en bdd con dicho email (Si no existe, se lanza UsernameNotFoundException)
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            //Verificamos validez del token (email coincidente y no expirado)
            if (jwtService.isTokenValid(token, userDetails)) {
                //Si es valido: Creamos el objeto de autenticacion
                //Este objeto comunica a Spring que el usuario esta autenticado
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, //Pasamos nuestro usuario
                                null, //Credenciales (null porque ya validamos el token)
                                userDetails.getAuthorities() //Roles (no tenemos ninguno por ahora)
                        );

                //Pasamos esta autenticacion al SecurityContext
                //El Security Context es como la "memoria" de Spring para este request, si lo introducimos en
                //el, Spring ya sabe que el usuario esta autenticado para el resto del ciclo de vida del request
                SecurityContextHolder.getContext().setAuthentication(authToken);

                //Arriba comprobamos con un if si el .getAuthentication() == null, pues a este paso nos
                //referiamos. Spring ya sabe si un usuario esta autenticado o no gracias al SecurityContext
            }
        }

        //Pasamos al siguiente filtro (nosotros como no tenemos mas, pasamos directamente al controller)
        filterChain.doFilter(request, response);
        //En este caso salimos del metodo con una autenticacion nueva hecha
    }
}
