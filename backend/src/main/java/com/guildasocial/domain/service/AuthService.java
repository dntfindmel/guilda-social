package com.guildasocial.domain.service;

import com.guildasocial.api.dto.request.LoginRequestDTO;
import com.guildasocial.api.dto.response.LoginResponseDTO;
import com.guildasocial.api.exception.BusinessException;
import com.guildasocial.domain.model.Usuario;
import com.guildasocial.domain.repository.UsuarioRepository;
import com.guildasocial.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    private final UsuarioRepository usuarioRepository;

    public AuthService(AuthenticationManager authenticationManager,
                       JwtTokenProvider tokenProvider,
                       UsuarioRepository usuarioRepository) {
        this.authenticationManager = authenticationManager;
        this.tokenProvider = tokenProvider;
        this.usuarioRepository = usuarioRepository;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO request) {
        System.out.println("=== INICIANDO LOGIN ===");
        System.out.println("Email: " + request.getEmail());
        System.out.println("Senha: " + request.getSenha());

        try {
            // 1. Verificar se o usuário existe no banco
            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElse(null);

            if (usuario == null) {
                System.out.println("❌ Usuário NÃO encontrado: " + request.getEmail());
                throw new BusinessException("E-mail ou senha inválidos");
            }

            System.out.println("✅ Usuário encontrado: " + usuario.getEmail());
            System.out.println("Senha no banco: " + usuario.getSenha());
            System.out.println("Senha informada: " + request.getSenha());

            // 2. Comparar senha diretamente (sem criptografia)
            if (!usuario.getSenha().equals(request.getSenha())) {
                System.out.println("❌ Senha não confere!");
                throw new BusinessException("E-mail ou senha inválidos");
            }

            System.out.println("✅ Senha confere!");

            // 3. Gerar autenticação manual
            Authentication authentication = new UsernamePasswordAuthenticationToken(
                usuario.getEmail(),
                usuario.getSenha()
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            // 4. Gerar token JWT
            String token = tokenProvider.generateToken(authentication);
            System.out.println("✅ Token gerado: " + token.substring(0, 30) + "...");

            // 5. Retornar resposta
            LoginResponseDTO response = new LoginResponseDTO();
            response.setToken(token);
            response.setType("Bearer");
            response.setUsuarioId(usuario.getId());
            response.setNome(usuario.getNome());
            response.setEmail(usuario.getEmail());

            System.out.println("=== LOGIN BEM-SUCEDIDO ===");
            return response;

        } catch (Exception e) {
            System.out.println("❌ Erro no login: " + e.getMessage());
            e.printStackTrace();
            throw new BusinessException("E-mail ou senha inválidos");
        }
    }
}
