package com.example.utils;

import org.junit.jupiter.api.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.List;
import java.io.FileWriter;


import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MovimentosUtilsTest {

    private File movimentosFile;

    @BeforeAll
    void setup() throws IOException {
        // Inicializar pasta e ficheiros
        FileUtils.initialize();
        movimentosFile = FileUtils.getMovimentosFile();

        // Limpar ficheiro antes de cada teste
        try (FileWriter fw = new FileWriter(movimentosFile, false)) {
            fw.write(""); // limpar conteúdo
        }
    }

    @Test
    void testRegistarEntrada() throws IOException {
        LocalDateTime agora = LocalDateTime.now();
        MovimentosUtils.registarEntrada("AA-00-00", agora);

        List<String> linhas = Files.readAllLines(movimentosFile.toPath());
        assertFalse(linhas.isEmpty());
        String ultima = linhas.get(linhas.size() - 1);
        assertTrue(ultima.contains("ENTRADA"));
        assertTrue(ultima.contains("AA-00-00"));
        assertTrue(ultima.contains(agora.toString()));
    }

    @Test
    void testRegistarSaida() throws IOException {
        LocalDateTime entrada = LocalDateTime.now().minusHours(2);
        LocalDateTime saida = LocalDateTime.now();
        double valor = 3.5;

        MovimentosUtils.registarSaida("BB-11-22", entrada, saida, valor);

        List<String> linhas = Files.readAllLines(movimentosFile.toPath());
        assertFalse(linhas.isEmpty());
        String ultima = linhas.get(linhas.size() - 1);
        assertTrue(ultima.contains("SAIDA"));
        assertTrue(ultima.contains("BB-11-22"));
        assertTrue(ultima.contains(entrada.toString()));
        assertTrue(ultima.contains(saida.toString()));
        assertTrue(ultima.contains(Double.toString(valor)));
    }

    @Test
    void testEscritaNoFicheiroAcumulaLinhas() throws IOException {
        int linhasAntes = (int) Files.lines(movimentosFile.toPath()).count();

        LocalDateTime agora = LocalDateTime.now();
        MovimentosUtils.registarEntrada("CC-22-33", agora);

        int linhasDepois = (int) Files.lines(movimentosFile.toPath()).count();
        assertEquals(linhasAntes + 1, linhasDepois);
    }
}

