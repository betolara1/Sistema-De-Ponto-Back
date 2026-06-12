package betolara1.Ponto.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import betolara1.Ponto.dto.LoginDTO;
import com.betolara1.jwt_package.security.JwtUtil;

import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Value("${auth.admin.user}")
    private String cpf;

    @Value("${auth.admin.password}")
    private String password;

    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginDTO request) {
        // Em vez de .equals(), você usa o encoder passwordEncoder.matches(senha_pura, senha_do_banco_criptografada)
        if (cpf.equals(request.cpf()) && passwordEncoder.matches(request.password(), password)) {
            String token = jwtUtil.generateToken(cpf);
            return ResponseEntity.ok(token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha inválida!");
    }
}