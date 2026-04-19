package adlf.register_login.api.controller;

import adlf.register_login.api.dto.UsuarioResponseDTO;
import adlf.register_login.api.service.UsuarioService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    //Mediante este controller, pruebo la utilidad del token obtenido. En caso de estar autenticado, puedo ver todos
    //los usuarios

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<UsuarioResponseDTO>> obtenerTodos(){
        return ResponseEntity.ok(service.obtenerTodos());
    }

}
