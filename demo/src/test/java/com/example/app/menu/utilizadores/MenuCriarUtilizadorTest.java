package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.FileUtils;
import com.example.utils.PasswordUtils;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;

class MenuCriarUtilizadorTest {

    @BeforeAll
    static void setupMocks() {
        // Mock das nacionalidades para ficar estável no teste
        mockStatic(FileUtils.class).when(FileUtils::carregarNacionalidades)
                .thenReturn(Arrays.asList("Portugal", "Brasil", "Angola"));

        // Mock do PasswordUtils
        mockStatic(PasswordUtils.class).when(() -> PasswordUtils.hash("1234"))
                .thenReturn("HASH1234");
    }

    @Test
    void testCriacaoUtilizadorSucesso() {

        UserService userService = mock(UserService.class);

        MenuCriarUtilizador menu = new MenuCriarUtilizador(userService);

        Utilizador novo = menu.criarUtilizadorTest(
                "joao123",
                "1234",
                "Joao",
                "Silva",
                "Funcionário",
                "Portugal",
                "1995-05-20",
                800,
                "08:00",
                "17:00",
                "operador"
        );

        assertNotNull(novo);
        assertEquals("joao123", novo.getUsername());
        assertEquals("HASH1234", novo.getPassword());
        assertEquals("Joao", novo.getNome());
        assertEquals("Portugal", novo.getNacionalidade());
        assertEquals("operador", novo.getTipo());

        verify(userService, times(1)).addUser(novo);
    }

    @Test
    void testCriacaoUtilizadorNacionalidadeInvalida() {

        UserService userService = mock(UserService.class);
        MenuCriarUtilizador menu = new MenuCriarUtilizador(userService);

        Utilizador novo = menu.criarUtilizadorTest(
                "teste",
                "1234",
                "Ana",
                "Costa",
                "Teste",
                "França",  // inválida
                "1990-01-01",
                900,
                "09:00",
                "17:00",
                "gerente"
        );

        assertNull(novo);
        verify(userService, never()).addUser(any());
    }

    @Test
    void testCriacaoUtilizadorDataInvalida() {

        UserService userService = mock(UserService.class);
        MenuCriarUtilizador menu = new MenuCriarUtilizador(userService);

        Utilizador novo = menu.criarUtilizadorTest(
                "teste",
                "1234",
                "Ana",
                "Costa",
                "Teste",
                "Portugal",
                "1990-99-99", // inválida
                900,
                "09:00",
                "17:00",
                "gerente"
        );

        assertNull(novo);
        verify(userService, never()).addUser(any());
    }
}
