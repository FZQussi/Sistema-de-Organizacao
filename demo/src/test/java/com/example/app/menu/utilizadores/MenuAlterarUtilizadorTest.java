package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuAlterarUtilizadorTest {

    @Test
    void testAlterarUtilizadorSucesso() {

        // Mock do UserService
        UserService userService = mock(UserService.class);

        // Criar utilizador existente
        Utilizador user = new Utilizador();
        user.setUsername("joao");
        user.setNome("João");
        user.setSobrenome("Silva");
        user.setDescricao("Funcionario");
        user.setNacionalidade("Portugal");
        user.setDataNascimento("1990-01-01");
        user.setSalario(750);
        user.setTurnoEntrada("09:00");
        user.setTurnoSaida("17:00");
        user.setTipo("operador");

        // Mockar retorno do serviço
        when(userService.getByUsername("joao")).thenReturn(user);

        MenuAlterarUtilizador menu = new MenuAlterarUtilizador(userService);

        // Chamar método testável
        boolean ok = menu.alterarUtilizador(
                "joao",
                "João Pedro",
                "",
                "",
                "Brasil",
                "",
                "900",
                "",
                "",
                "gerente"
        );

        assertTrue(ok);
        assertEquals("João Pedro", user.getNome());   // atualizou nome
        assertEquals("Brasil", user.getNacionalidade());
        assertEquals(900, user.getSalario());
        assertEquals("gerente", user.getTipo());

        // Verifica se o serviço foi chamado
        verify(userService, times(1)).updateUser("joao", user);
    }

    @Test
    void testAlterarUtilizadorNaoExiste() {

        UserService userService = mock(UserService.class);
        when(userService.getByUsername("xpto")).thenReturn(null);

        MenuAlterarUtilizador menu = new MenuAlterarUtilizador(userService);

        boolean ok = menu.alterarUtilizador(
                "xpto", "", "", "", "", "", "", "", "", ""
        );

        assertFalse(ok);
        verify(userService, never()).updateUser(anyString(), any());
    }
}
