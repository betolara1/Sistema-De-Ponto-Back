package betolara1.Ponto.controller;

import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import betolara1.Ponto.dto.LoginDTO;
import betolara1.Ponto.dto.response.LoginResponseDTO;
import betolara1.Ponto.model.Colaboradores;
import betolara1.Ponto.repository.ColaboradoresRepository;

import jakarta.validation.Valid;

import com.betolara1.jwt_package.security.JwtUtil;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ColaboradoresRepository colaboradoresRepository;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, ColaboradoresRepository colaboradoresRepository) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.colaboradoresRepository = colaboradoresRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO request) {
        Optional<Colaboradores> colab = colaboradoresRepository.findByCpfContainingIgnoreCase(request.cpf());
        
        if(colab.isPresent()){
            Colaboradores colaborador = colab.get();

            // Em vez de .equals(), você usa o encoder passwordEncoder.matches(senha_pura, senha_do_banco_criptografada)
            if (passwordEncoder.matches(request.password(), colaborador.getSenha())) {
                String token = jwtUtil.generateToken(colaborador.getCpf());

                return ResponseEntity.ok(new LoginResponseDTO(token));
            }
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválida!"); 
    }
}