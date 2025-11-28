package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.PaymentService;
import com.example.service.UserService;
import org.junit.jupiter.api.*;
import java.io.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuPagamentosTest {

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

    // injeta mocks via reflexão (pois PaymentService é criado dentro do menu)
    private void inject(Object target, String field, Object value) throws Exception {
        var f = target.getClass().getDeclaredField(field);
        f.setAccessible(true);
        f.set(target, value);
    }

    // -------------------------------
    // TESTE 1 — Utilizador não existe
    // -------------------------------
    @Test
    void testUtilizadorInexistente() throws Exception {

        String input = "2024\n5\nnaoexiste\n";
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        when(userService.getByUsername("naoexiste")).thenReturn(null);

        MenuPagamentos menu = new MenuPagamentos(userService);

        menu.mostrar();

        String output = saida.toString();
        assertTrue(output.contains("❌ Utilizador não encontrado"));
    }

    // -------------------------------------
    // TESTE 2 — Pagamento calculado corretamente
    // -------------------------------------
    @Test
    void testPagamentoChamado() throws Exception {

        String input = "2024\n3\njoao\n\n"; // último \n é do "pressione Enter"
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        Utilizador u = new Utilizador(
            "joao", "pass", "OPERADOR",
            "João", "Silva",
            "descricao", "Portugal",
            "1990-01-01",
            900.0,
            "08:00", "16:00"
        );

        when(userService.getByUsername("joao")).thenReturn(u);

        PaymentService paymentMock = mock(PaymentService.class);

        MenuPagamentos menu = new MenuPagamentos(userService);

        // injeta mock
        inject(menu, "paymentService", paymentMock);

        menu.mostrar();

        verify(paymentMock, times(1))
                .calcularPagamentoMensal(u, 2024, 3);

        assertTrue(saida.toString().contains("Calculando pagamento"));
    }

    // ----------------------------------------
    // TESTE 3 — Erro: input inválido (texto no ano)
    // ----------------------------------------
    @Test
    void testAnoInvalido() {

        String input = "abc\n"; // vai falhar no ano e lançar NumberFormatException
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        MenuPagamentos menu = new MenuPagamentos(mock(UserService.class));

        assertThrows(NumberFormatException.class, menu::mostrar);
    }

    // ----------------------------------------
    // TESTE 4 — Erro: input inválido (mês fora do intervalo)
    // ----------------------------------------
    @Test
    void testMesInvalido() {

        String input = "2024\n99\njoao\n"; // mês inválido mas teu código aceita (vai passar)
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        UserService userService = mock(UserService.class);
        when(userService.getByUsername("joao")).thenReturn(null);

        MenuPagamentos menu = new MenuPagamentos(userService);

        menu.mostrar();

        assertTrue(saida.toString().contains("❌ Utilizador não encontrado"));
    }

}
