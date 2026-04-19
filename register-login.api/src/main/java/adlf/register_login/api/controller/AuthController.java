package adlf.register_login.api.controller;

import adlf.register_login.api.dto.AuthResponseDTO;
import adlf.register_login.api.dto.LoginRequestDTO;
import adlf.register_login.api.dto.RegisterRequestDTO;
import adlf.register_login.api.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService){
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@Valid @RequestBody RegisterRequestDTO dto){
        return ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody LoginRequestDTO dto){
        return ResponseEntity.ok(authService.login(dto));
    }

}
