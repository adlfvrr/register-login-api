package adlf.register_login.api.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    /*
    Recordatorio: El cuerpo de un token esta compuesto por:
    Header --> Indica el algoritmo utilizado para firmar,
    Payload --> Contiene datos útiles, como ID del usuario, rol y fecha de expiración,
    Signature --> Es la firma, resultado de mezclar header + payload con una clave secreta
     */


    //Utilizamos el valor de jwt.secret de nuestro application.properties
    @Value("${jwt.secret}")
    private String secretKey;

    //Utilizamos el valor de jwt.expiration (en este caso de 24 horas en milisegundos) de nuestro application.properties
    @Value("${jwt.expiration}")
    private long expiration;

    //Convertimos nuestra secretKey en un formato de Key entendible por JJWT (no profundicemos tanto)
    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    //Creamos Token
    public String generateToken(UserDetails userDetails) {
        return Jwts.builder() //Iniciamos la construccion con .builder del token
                .setSubject(userDetails.getUsername()) //Guardamos el username (email) del usuario dentro del token
                .setIssuedAt(new Date()) //Establecemos creación (tiempo actual) del token
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) //Establecemos expiración del token con nuestro expiration
                .signWith(getSigningKey(), SignatureAlgorithm.HS256) // Firma el token usando clave secreta y algoritmo HS256
                .compact(); //Termina la construcción y convierte el token en un String
    }

    //Obtenemos el username (email) del token
    public String extractEmail(String token){
        return Jwts.parserBuilder() //Crea un parser de JWT configurado con nuestra clave secreta para poder leer la firma
                .setSigningKey(getSigningKey()) //Indica al parser que clave utiliza la firma para poder verificarla
                .build() //Construye el parser
                .parseClaimsJws(token) //Analiza el token, valida la firma y extrae el cuerpo (Un claim)
                .getBody() //Obtiene el "payload" del token
                .getSubject(); //Extrae el "sub" del payload (nuestro subject = usuario)
    }

    //Preguntamos si el token esta expirado
    private boolean isTokenExpired(String token){
        Date expiracion = Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration(); //Lo mismo que extractEmail, solo que en este caso debemos extraer la expiracion
        return expiracion.before(new Date());
        //expiracion.before compara si la expiracion fue antes de la Date actual en milisegundos
        //Retorna true si la expiración es anterior a la actual (es decir, vencida)
    }
    //Preguntamos si el token sigue siendo valido
    public boolean isTokenValid(String token, UserDetails userDetails){
        final String email = extractEmail(token); //extraemos email
        return email.equals(userDetails.getUsername()) && !isTokenExpired(token);
        //Preguntamos si el email del token es el mismo que el de nuestro User y, a su vez, si el token venció
        //Si el usuario coincide y el token no vencio, retorna true
    }

}
