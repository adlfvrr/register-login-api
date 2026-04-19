package adlf.register_login.api.service;

import adlf.register_login.api.dto.UsuarioMapper;
import adlf.register_login.api.dto.UsuarioResponseDTO;
import adlf.register_login.api.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

    //Creamos el service para nuestro controller de autenticados
    private final UsuarioRepository repository;

    public UsuarioService(UsuarioRepository repository){
        this.repository = repository;
    }

    public List<UsuarioResponseDTO> obtenerTodos(){
        return repository.findAll().stream()
                .map(UsuarioMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

}
