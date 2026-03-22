package com.guildasocial.security;

import org.springframework.stereotype.Component;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

@Component
public class BCryptPasswordEncoder implements PasswordEncoder {

    private static final int ITERATIONS = 10000;
    private static final int KEY_LENGTH = 256;
    private static final SecureRandom RANDOM = new SecureRandom();

    @Override
    public String encode(String rawPassword) {
        try {
            byte[] salt = new byte[16];
            RANDOM.nextBytes(salt);
            String saltBase64 = Base64.getEncoder().encodeToString(salt);
            String hash = hashPassword(rawPassword, salt);
            return ITERATIONS + ":" + saltBase64 + ":" + hash;
        } catch (Exception e) {
            throw new RuntimeException("Erro ao codificar senha", e);
        }
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        try {
            String[] parts = encodedPassword.split(":");
            if (parts.length != 3) return false;

            int iterations = Integer.parseInt(parts[0]);
            byte[] salt = Base64.getDecoder().decode(parts[1]);
            String expectedHash = parts[2];
            String actualHash = hashPassword(rawPassword, salt, iterations);

            return actualHash.equals(expectedHash);
        } catch (Exception e) {
            return false;
        }
    }

    private String hashPassword(String password, byte[] salt)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        return hashPassword(password, salt, ITERATIONS);
    }

    private String hashPassword(String password, byte[] salt, int iterations)
            throws NoSuchAlgorithmException, InvalidKeySpecException {
        PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, KEY_LENGTH);
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
        byte[] hash = skf.generateSecret(spec).getEncoded();
        return Base64.getEncoder().encodeToString(hash);
    }
}
