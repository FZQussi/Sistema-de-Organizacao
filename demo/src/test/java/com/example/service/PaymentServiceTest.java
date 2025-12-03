package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    private PaymentService paymentService;
    private Path turnosTemp;
    private Path pagamentosTemp;
    private MockedStatic<FileUtils> fileMock;

    @BeforeEach
    void setup() throws Exception {

        paymentService = new PaymentService();

        // ficheiros temporários
        turnosTemp = Files.createTempFile("turnos-test", ".txt");
        pagamentosTemp = Files.createTempFile("pagamentos-test", ".txt");

        // mock de FileUtils
        fileMock = mockStatic(FileUtils.class);

        fileMock.when(FileUtils::getTurnosFile).thenReturn(turnosTemp.toFile());
        fileMock.when(FileUtils::getPagamentosFile).thenReturn(pagamentosTemp.toFile());
    }

    @AfterEach
    void cleanup() throws Exception {
        fileMock.close();
        Files.deleteIfExists(turnosTemp);
        Files.deleteIfExists(pagamentosTemp);
    }

    // -------------------------------------------------------
    // TESTE: Calcular horas trabalhadas + atrasos + gravação
    // -------------------------------------------------------
    @Test
    void testCalcularPagamentoMensal_ComValoresValidos() throws Exception {

        // Simular conteúdo do turnos.txt
        String conteudoTurnos =
                """
                [2025-01] admin
                Total trabalhado: 5h 30min
                Atraso: 15

                [2025-01] outro
                Total trabalhado: 2h 00min
                Atraso: 0
                """;

        Files.writeString(turnosTemp, conteudoTurnos);

        Utilizador u = new Utilizador(
                "admin", "pass", "gerente", "nome", "sob",
                "", "PT", "1990", 10.0, "08:00", "17:00"
        );

        paymentService.calcularPagamentoMensal(u, 2025, 1);

        // Ler ficheiro gerado
        String result = Files.readString(pagamentosTemp);

        // Verificações importantes
        assertTrue(result.contains("[2025-01] admin"));
        assertTrue(result.contains("Bruto: 55.00"));
        assertTrue(result.contains("Desconto: -2.50"));
        assertTrue(result.contains("Final: 52.50"));
    }

    // -------------------------------------------------------
    // TESTE: quando não há blocos do utilizador → tudo zero
    // -------------------------------------------------------
    @Test
    void testCalcularPagamentoMensal_SemRegistos() throws Exception {

        Files.writeString(turnosTemp, ""); // vazio

        Utilizador u = new Utilizador(
                "ghost", "123", "operador", "n", "s",
                "", "PT", "1990", 10.0, "08:00", "17:00"
        );

        paymentService.calcularPagamentoMensal(u, 2025, 1);

        String result = Files.readString(pagamentosTemp);

        assertTrue(result.contains("[2025-01] ghost"));
        assertTrue(result.contains("Bruto: 0.00"));
        assertTrue(result.contains("Final: 0.00"));
    }

    // -------------------------------------------------------
    // TESTE: Verifica parsing de múltiplos blocos do mesmo user
    // -------------------------------------------------------
    @Test
    void testCalcularPagamentoMensal_MultiplosDiasSomados() throws Exception {

        String conteudo =
                """
                [2025-02] admin
                Total trabalhado: 1h 00min
                Atraso: 5

                [2025-02] admin
                Total trabalhado: 2h 30min
                Atraso: 10
                """;

        Files.writeString(turnosTemp, conteudo);

        Utilizador u = new Utilizador(
                "admin", "pass", "operador", "a", "b", "", "PT",
                "1990", 12.0, "08:00", "17:00"
        );

        paymentService.calcularPagamentoMensal(u, 2025, 2);

        String out = Files.readString(pagamentosTemp);

        // Total: (1h + 2.5h) = 3.5h → 3.5 * 12 = 42.00
        assertTrue(out.contains("Bruto: 42.00"));

        // Atrasos: 5 + 10 = 15min → 0.25h → desconto = 3.00
        assertTrue(out.contains("Desconto: -3.00"));

        assertTrue(out.contains("Final: 39.00"));
    }

    // -------------------------------------------------------
    // TESTE: IO Exception em FileUtils.getPagamentosFile()
    // -------------------------------------------------------
    @Test
    void testCalcularPagamentoMensal_IOException() throws Exception {

        fileMock.close();

        MockedStatic<FileUtils> mockErro = mockStatic(FileUtils.class);

        mockErro.when(FileUtils::getTurnosFile).thenReturn(turnosTemp.toFile());
        mockErro.when(FileUtils::getPagamentosFile)
                .thenReturn(new File("/erro/caminho/inválido/pag.txt"));

        Utilizador u = new Utilizador(
                "admin","123","gerente","a","b","","PT","1990",
                10.0, "08:00", "17:00"
        );

        assertDoesNotThrow(() ->
                paymentService.calcularPagamentoMensal(u, 2025, 1)
        );

        mockErro.close();
    }
}
