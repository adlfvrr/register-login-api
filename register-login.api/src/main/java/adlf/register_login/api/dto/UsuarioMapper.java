package adlf.register_login.api.dto;

import adlf.register_login.api.entity.Usuario;

public class UsuarioMapper {

    //Creamos un UsuarioMapper para convertir todas las entitys de nuestro metodo findAll del Repository a
    // ResponseDTO

    public static UsuarioResponseDTO toResponseDTO(Usuario entity){
        return new UsuarioResponseDTO(entity.getId(), entity.getEmail());
    }

}
