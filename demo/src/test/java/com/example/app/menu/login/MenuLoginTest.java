package com.example.app.menu.login;

import com.example.model.Utilizador;
import com.example.service.AuthService;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuLoginTest {

    @Test
    void testLoginSucesso() {

        // Mock do AuthService
        AuthService auth = mock(AuthService.class);

        // Criar utilizador fake usando construtor vazio
        Utilizador fakeUser = new Utilizador();
        fakeUser.setUsername("john");
        fakeUser.setPassword("123");
        fakeUser.setTipo("operador");

        // Definir comportamento do mock
        when(auth.login("john", "123")).thenReturn(fakeUser);

        // Criar o menu com o mock
        MenuLogin menu = new MenuLogin(auth);

        // Chamar método testável
        Utilizador result = menu.login("john", "123");

        // Verificar resultados
        assertNotNull(result);
        assertEquals("john", result.getUsername());
        assertEquals("operador", result.getTipo());
    }

    @Test
    void testLoginFalha() {

        AuthService auth = mock(AuthService.class);

        // Mock devolve null para credenciais erradas
        when(auth.login("a", "b")).thenReturn(null);

        MenuLogin menu = new MenuLogin(auth);

        Utilizador result = menu.login("a", "b");

        assertNull(result);
    }
}
