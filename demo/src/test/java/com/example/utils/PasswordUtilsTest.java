package com.example.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PasswordUtilsTest {

    @Test
    void testHashAndVerifySuccess() {
        String password = "MinhaSenha123!";
        String hashed = PasswordUtils.hash(password);

        assertNotNull(hashed, "O hash não deve ser nulo");
        assertTrue(PasswordUtils.verify(password, hashed), "A verificação deve retornar true para a password correta");
    }

    @Test
    void testVerifyFailsForWrongPassword() {
        String password = "SenhaCerta";
        String wrongPassword = "SenhaErrada";

        String hashed = PasswordUtils.hash(password);

        assertFalse(PasswordUtils.verify(wrongPassword, hashed), "A verificação deve retornar false para uma password errada");
    }

    @Test
    void testHashProducesDifferentResults() {
        String password = "Teste123";

        String hash1 = PasswordUtils.hash(password);
        String hash2 = PasswordUtils.hash(password);

        // Cada hash deve ser diferente por causa do salt
        assertNotEquals(hash1, hash2, "Hashes devem ser diferentes devido ao salt");
    }
}
