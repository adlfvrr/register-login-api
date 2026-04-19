package adlf.register_login.api.exception;

public class UsuarioExistenteException extends RuntimeException {
    public UsuarioExistenteException() {
        super("El usuario a registrar ya existe");
    }
}
