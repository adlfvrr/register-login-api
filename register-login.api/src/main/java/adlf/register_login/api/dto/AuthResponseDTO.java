package adlf.register_login.api.dto;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;

public class AuthResponseDTO {

    //Con este DTO, le mostraremos al usuario su email y su token para que pueda acceder a rutas que necesitan de
    //autenticacion

    private String token;

    private String email;

    public AuthResponseDTO(String token, String email){
        this.token = token;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public String getToken() {
        return token;
    }
}
