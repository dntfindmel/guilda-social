package com.guildasocial.domain.service;

import org.springframework.stereotype.Service;

@Service
public class EmailService {
    public void enviarEmailRecuperacao(String email, String token) {
        String linkRecuperacao = "http://localhost:3030/redefinir-senha?token=" + token;
        System.out.println("========================================");
        System.out.println("📧 ENVIO DE E-MAIL (SIMULADO)");
        System.out.println("Para: " + email);
        System.out.println("Assunto: Recuperação de Senha - Guilda Social");
        System.out.println("Mensagem: Clique no link para redefinir sua senha:");
        System.out.println(linkRecuperacao);
        System.out.println("========================================");
    }
}
