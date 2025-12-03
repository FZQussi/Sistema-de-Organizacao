package com.example.service;

import com.example.model.MovimentoEstacionamento;
import com.example.utils.FileUtils;
import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestaoPagamentosTest {

    private Path tempFile;
    private MockedStatic<FileUtils> fileUtilsMock;

    @BeforeEach
    void setup() throws IOException {
        // Criar ficheiro temporário
        tempFile = Files.createTempFile("pagamentos-test", ".txt");

        // Mock do FileUtils.getPagamentosFile()
        fileUtilsMock = mockStatic(FileUtils.class);
        fileUtilsMock.when(FileUtils::getPagamentosFile).thenReturn(tempFile.toFile());
    }

    @AfterEach
    void cleanup() {
        fileUtilsMock.close();
        tempFile.toFile().delete();
    }

    @Test
    void testRegistarPagamentoEscreveNoFicheiro() throws IOException {

        LocalDateTime entrada = LocalDateTime.now().minusHours(2);
        LocalDateTime saída = LocalDateTime.now();
        double valor = 3.50;

        GestaoPagamentos.registarPagamento("AA-00-BB", entrada, saída, valor);

        // Ler conteúdo escrito
        String conteudo = Files.readString(tempFile);

        assertTrue(conteudo.contains("AA-00-BB"));
        assertTrue(conteudo.contains(String.valueOf(valor)));
        assertTrue(conteudo.contains(entrada.toString()));
        assertTrue(conteudo.contains(saída.toString()));
    }

    @Test
    void testRegistarPagamentoVariosRegistos() throws IOException {

        LocalDateTime entrada1 = LocalDateTime.now().minusHours(3);
        LocalDateTime saída1 = LocalDateTime.now().minusHours(1);

        LocalDateTime entrada2 = LocalDateTime.now().minusHours(5);
        LocalDateTime saída2 = LocalDateTime.now();

        GestaoPagamentos.registarPagamento("CAR-123", entrada1, saída1, 2.0);
        GestaoPagamentos.registarPagamento("XYZ-999", entrada2, saída2, 5.0);

        String conteudo = Files.readString(tempFile);

        assertTrue(conteudo.contains("CAR-123"));
        assertTrue(conteudo.contains("XYZ-999"));
    }

    @Test
    void testRegistarPagamentoLidaComIOException() throws IOException {

        // Mock que FAZ o FileWriter falhar
        fileUtilsMock.close(); // libertar mock atual

        MockedStatic<FileUtils> errorMock = mockStatic(FileUtils.class);
        errorMock.when(FileUtils::getPagamentosFile).thenReturn(new File("/caminho/invalido/123/arquivo.txt"));

        assertDoesNotThrow(() ->
                GestaoPagamentos.registarPagamento(
                        "AA-00-AA",
                        LocalDateTime.now().minusMinutes(5),
                        LocalDateTime.now(),
                        1.2
                )
        );

        errorMock.close();
    }
}
