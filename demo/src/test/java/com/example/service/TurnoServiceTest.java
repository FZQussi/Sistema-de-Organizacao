package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;
import com.example.utils.CalculadoraTempo;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.io.File;
import java.io.FileWriter;
import java.time.*;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class TurnoServiceTest {

    private TurnoService turnoService;
    private Utilizador userMock;

    @BeforeEach
    void setup() {
        turnoService = new TurnoService();

        userMock = mock(Utilizador.class);
        when(userMock.getUsername()).thenReturn("joao");
        when(userMock.getTipo()).thenReturn("Funcionario");
        when(userMock.getTurnoEntrada()).thenReturn("08:00");
        when(userMock.getTurnoSaida()).thenReturn("16:00");
    }

    @Test
    void testRegistarEntradaEscreveNoFicheiro() throws Exception {

        File mockFile = mock(File.class);
        FileWriter mockWriter = mock(FileWriter.class);

        try (MockedStatic<FileUtils> fileMock = mockStatic(FileUtils.class);
             MockedStatic<LocalDateTime> timeMock = mockStatic(LocalDateTime.class)) {

            // Mock do ficheiro
            fileMock.when(FileUtils::getTurnosFile).thenReturn(mockFile);

            // Tempo fixo
            LocalDateTime fixed = LocalDateTime.of(2024, 1, 10, 8, 10);
            timeMock.when(LocalDateTime::now).thenReturn(fixed);

            // Substituir construtor de FileWriter
            MockedStatic<CalculadoraTempo> tempoMock = mockStatic(CalculadoraTempo.class);
            tempoMock.when(() -> CalculadoraTempo.minutosDiferenca(any(), any())).thenReturn(10L);

            // Mock manual via spy
            FileWriter fw = spy(new FileWriter(File.createTempFile("turno", "txt")));
            try (MockedStatic<FileWriter> writerMock = mockStatic(FileWriter.class)) {
                writerMock.when(() -> new FileWriter(mockFile, true)).thenReturn(fw);

                turnoService.registarEntrada(userMock);

                verify(fw, atLeastOnce()).write(contains("Entrada prevista"));
            }
        }
    }

    @Test
    void testRegistarSaidaEscreveNoFicheiro() throws Exception {

        File mockFile = mock(File.class);

        try (MockedStatic<FileUtils> fileMock = mockStatic(FileUtils.class);
             MockedStatic<LocalDateTime> timeMock = mockStatic(LocalDateTime.class);
             MockedStatic<CalculadoraTempo> tempoMock = mockStatic(CalculadoraTempo.class)) {

            fileMock.when(FileUtils::getTurnosFile).thenReturn(mockFile);

            // Entrada fixa
            LocalDateTime entradaFix = LocalDateTime.of(2024, 1, 10, 8, 0);
            // Saída fixa
            LocalDateTime saidaFix = LocalDateTime.of(2024, 1, 10, 16, 30);

            // Mock das horas
            timeMock.when(LocalDateTime::now)
                .thenReturn(entradaFix)  // primeiro chamada no registarEntrada
                .thenReturn(saidaFix);   // segunda chamada no registarSaida

            tempoMock.when(() -> CalculadoraTempo.minutosDiferenca(any(), any()))
                     .thenReturn(30L);

            FileWriter fw = spy(new FileWriter(File.createTempFile("turno2", "txt")));

            try (MockedStatic<FileWriter> writerMock = mockStatic(FileWriter.class)) {
                writerMock.when(() -> new FileWriter(mockFile, true)).thenReturn(fw);

                turnoService.registarEntrada(userMock);
                turnoService.registarSaida(userMock);

                verify(fw, atLeastOnce()).write(contains("Saída prevista"));
                verify(fw, atLeastOnce()).write(contains("Total trabalhado"));
            }
        }
    }
}
