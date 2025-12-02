package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.io.*;

class MenuRemoverUtilizadorTest {

    private final ByteArrayOutputStream saida = new ByteArrayOutputStream();
    private PrintStream consolaOriginal;

    @BeforeEach
    void setUp() {
        consolaOriginal = System.out;
        System.setOut(new PrintStream(saida));
    }

    @AfterEach
    void tearDown() {
        System.setOut(consolaOriginal);
    }

    // Helper para criar utilizador
    private Utilizador criarUtilizador() {
        Utilizador u = new Utilizador();
        u.setUsername("joao");
        u.setNome("João");
        u.setSobrenome("Silva");
        u.setTipo("OPERADOR");
        return u;
    }

    // ----------------------------------------------------------
    // TESTE 1 — Cancelar com "0"
    // ----------------------------------------------------------
    @Test
    void testCancelarComZero() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        MenuRemoverUtilizador menu = new MenuRemoverUtilizador(userService);

        menu.mostrar();

        String out = saida.toString();
        assertTrue(out.contains("Ação cancelada"), "Deveria cancelar quando usa 0.");
        verify(userService, never()).removeUser(anyString());
    }

    // ----------------------------------------------------------
    // TESTE 2 — Utilizador não encontrado
    // ----------------------------------------------------------
    @Test
    void testUtilizadorNaoEncontrado() {
        String input = "joao\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        when(userService.getByUsername("joao")).thenReturn(null);

        MenuRemoverUtilizador menu = new MenuRemoverUtilizador(userService);
        menu.mostrar();

        String out = saida.toString();
        assertTrue(out.contains("Utilizador não encontrado"), "Deve avisar que não existe.");
    }

    // ----------------------------------------------------------
    // TESTE 3 — Encontrado mas remoção cancelada
    // ----------------------------------------------------------
    @Test
    void testCancelarRemocao() {
        String input =
                "joao\n" +   // username
                "n\n";       // confirmação

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        when(userService.getByUsername("joao")).thenReturn(criarUtilizador());

        MenuRemoverUtilizador menu = new MenuRemoverUtilizador(userService);
        menu.mostrar();

        String out = saida.toString();

        assertTrue(out.contains("Ação cancelada"), "Deve cancelar quando confirma n.");
        verify(userService, never()).removeUser(anyString());
    }

    // ----------------------------------------------------------
    // TESTE 4 — Remoção com sucesso
    // ----------------------------------------------------------
    @Test
    void testRemocaoComSucesso() {
        String input =
                "joao\n" +   // username
                "s\n";       // confirmação

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        when(userService.getByUsername("joao")).thenReturn(criarUtilizador());

        MenuRemoverUtilizador menu = new MenuRemoverUtilizador(userService);
        menu.mostrar();

        String out = saida.toString();

        verify(userService, times(1)).removeUser("joao");
        assertTrue(out.contains("Utilizador removido com sucesso"), "Deve indicar sucesso.");
    }

    // ----------------------------------------------------------
    // TESTE 5 — Confirmação inválida
    // ----------------------------------------------------------
    @Test
    void testConfirmacaoInvalida() {
        String input =
                "joao\n" +   // username
                "abc\n";     // confirmação inválida

        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        when(userService.getByUsername("joao")).thenReturn(criarUtilizador());

        MenuRemoverUtilizador menu = new MenuRemoverUtilizador(userService);
        menu.mostrar();

        String out = saida.toString();

        assertTrue(out.contains("Ação cancelada"), "Qualquer coisa diferente de 's' cancela.");
        verify(userService, never()).removeUser(anyString());
    }
}
