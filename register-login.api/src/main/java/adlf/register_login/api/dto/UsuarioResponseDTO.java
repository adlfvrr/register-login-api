package adlf.register_login.api.dto;

public class UsuarioResponseDTO {

    //Creo un nuevo DTO para probar un controller que, una vez estando autenticado, pueda ver todos los usuarios

    private Long id;
    private String email;

    public UsuarioResponseDTO(Long id, String email){
        this.id = id;
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
