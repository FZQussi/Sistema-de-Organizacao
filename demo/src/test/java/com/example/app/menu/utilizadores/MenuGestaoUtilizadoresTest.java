package com.example.app.menu.utilizadores;

import com.example.service.UserService;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;

import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuGestaoUtilizadoresTest {

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

    // --------------------
    // Classe fake para poder mockar os menus internos
    // --------------------
static class MenuGestaoFake extends MenuGestaoUtilizadores {

    Runnable acaoListar;
    Runnable acaoCriar;
    Runnable acaoAlterar;
    Runnable acaoRemover;
    Runnable acaoPagamentos;

    public MenuGestaoFake(UserService userService) {
        super(userService);
    }

    @Override
    protected MenuListagemUtilizadores criarMenuListagem() {
        MenuListagemUtilizadores mockMenu = mock(MenuListagemUtilizadores.class);
        doAnswer(inv -> { acaoListar.run(); return null; }).when(mockMenu).mostrar();
        return mockMenu;
    }

    @Override
    protected MenuCriarUtilizador criarMenuCriar() {
        MenuCriarUtilizador mockMenu = mock(MenuCriarUtilizador.class);
        doAnswer(inv -> { acaoCriar.run(); return null; }).when(mockMenu).mostrar();
        return mockMenu;
    }

    @Override
    protected MenuAlterarUtilizador criarMenuAlterar() {
        MenuAlterarUtilizador mockMenu = mock(MenuAlterarUtilizador.class);
        doAnswer(inv -> { acaoAlterar.run(); return null; }).when(mockMenu).mostrar();
        return mockMenu;
    }

    @Override
    protected MenuRemoverUtilizador criarMenuRemover() {
        MenuRemoverUtilizador mockMenu = mock(MenuRemoverUtilizador.class);
        doAnswer(inv -> { acaoRemover.run(); return null; }).when(mockMenu).mostrar();
        return mockMenu;
    }

    @Override
    protected MenuPagamentos criarMenuPagamentos() {
        MenuPagamentos mockMenu = mock(MenuPagamentos.class);
        doAnswer(inv -> { acaoPagamentos.run(); return null; }).when(mockMenu).mostrar();
        return mockMenu;
    }
}


    // -------------------------------------------------------------
    // TESTE 1 — Escolher opção 1 deve chamar MenuListagemUtilizadores
    // -------------------------------------------------------------
    @Test
    void testOpcaoListar() {
        String input = "1\n0\n"; // listar e depois sair
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        MenuGestaoFake menu = new MenuGestaoFake(userService);

        boolean[] chamado = {false};
        menu.acaoListar = () -> chamado[0] = true;

        menu.mostrar();

        assertTrue(chamado[0], "Menu de listagem deveria ter sido chamado");
    }

    // -------------------------------------------------------------
    // TESTE 2 — Entrada inválida (texto)
    // -------------------------------------------------------------
    @Test
    void testEntradaInvalida() {
        String input = "abc\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MenuGestaoFake menu = new MenuGestaoFake(mock(UserService.class));
        menu.mostrar();

        String out = saida.toString();

        assertTrue(out.contains("Opcão inválida"), "Deve avisar que a opção é inválida");
    }

    // -------------------------------------------------------------
    // TESTE 3 — Opção inexistente
    // -------------------------------------------------------------
    @Test
    void testOpcaoInexistente() {
        String input = "9\n0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MenuGestaoFake menu = new MenuGestaoFake(mock(UserService.class));
        menu.mostrar();

        assertTrue(saida.toString().contains("✖ Opcão inválida"), 
            "Deve avisar que a opção não existe");
    }

    // -------------------------------------------------------------
    // TESTE 4 — Opção Sair (0)
    // -------------------------------------------------------------
    @Test
    void testSair() {
        String input = "0\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MenuGestaoFake menu = new MenuGestaoFake(mock(UserService.class));
        menu.mostrar();

        assertTrue(saida.toString().contains("A voltar ao menu principal"),
            "Deve mostrar mensagem ao sair.");
    }
}
