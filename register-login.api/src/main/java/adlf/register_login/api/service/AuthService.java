package adlf.register_login.api.service;

import adlf.register_login.api.dto.AuthResponseDTO;
import adlf.register_login.api.dto.LoginRequestDTO;
import adlf.register_login.api.dto.RegisterRequestDTO;
import adlf.register_login.api.entity.Usuario;
import adlf.register_login.api.exception.UsuarioExistenteException;
import adlf.register_login.api.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    //Este sera nuestro service de autenticacion de usuarios

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        //Inyectamos
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    //Creamos el register
    public AuthResponseDTO register(RegisterRequestDTO dto){
        if(usuarioRepository.findByEmail(dto.getEmail()).isPresent()){
            //Si existe ya, lanzamos exception
            throw new UsuarioExistenteException();
        }

        //Creamos el usuario para persistirlo
        Usuario usuario = new Usuario();
        usuario.setEmail(dto.getEmail());
        //En la contraseña, NUNCA la guardamos en la bdd en texto plano, siempre encriptada
        usuario.setPassword(passwordEncoder.encode(dto.getPassword()));

        //Persistimos el usuario
        usuarioRepository.save(usuario);

        //Generamos su token
        String token = jwtService.generateToken(usuario);

        //Retornamos token y email
        return new AuthResponseDTO(token, usuario.getEmail());
    }

    //Creamos el login
    public AuthResponseDTO login(LoginRequestDTO dto){
        //Autenticamos si el email y la password son correctas, en caso de no, lanza exception
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getEmail(), dto.getPassword()));

        //En caso de que no haya exception: Buscamos el usuario, le generamos su token, y le devolvemos sus datos
        Usuario usuario = usuarioRepository.findByEmail(dto.getEmail()).orElseThrow();
        String token = jwtService.generateToken(usuario);
        return new AuthResponseDTO(token, usuario.getEmail());
    }

}
