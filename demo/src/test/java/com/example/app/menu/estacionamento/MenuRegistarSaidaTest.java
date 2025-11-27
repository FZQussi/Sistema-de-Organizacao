package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MenuRegistarSaidaTest {

    @Test
    void testMatriculaValida() {
        GestaoEstacionamento gestaoMock = mock(GestaoEstacionamento.class);

        Scanner sc = new Scanner("AA-12-34\n0\n");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        MenuRegistarSaida menu = new MenuRegistarSaida(gestaoMock, sc, out);

        when(gestaoMock.registrarSaida("AA-12-34")).thenReturn(true);

        menu.mostrar();

        String printed = output.toString();

        assertTrue(printed.contains("✔ Saída registada com sucesso!"));
        verify(gestaoMock, times(1)).registrarSaida("AA-12-34");
    }


    @Test
    void testMatriculaInvalida() {
        GestaoEstacionamento gestaoMock = mock(GestaoEstacionamento.class);

        Scanner sc = new Scanner("INVALIDA\nAA-12-34\n0\n");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        MenuRegistarSaida menu = new MenuRegistarSaida(gestaoMock, sc, out);

        when(gestaoMock.registrarSaida("AA-12-34")).thenReturn(true);

        menu.mostrar();

        String printed = output.toString();
        assertTrue(printed.contains("Matrícula inválida"));
        verify(gestaoMock, times(1)).registrarSaida("AA-12-34");
    }


    @Test
    void testPlacaNaoEncontrada() {
        GestaoEstacionamento gestaoMock = mock(GestaoEstacionamento.class);

        Scanner sc = new Scanner("AA-22-33\n0\n");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        MenuRegistarSaida menu = new MenuRegistarSaida(gestaoMock, sc, out);

        when(gestaoMock.registrarSaida("AA-22-33")).thenReturn(false);

        menu.mostrar();

        String printed = output.toString();
        assertTrue(printed.contains("Falha: Placa não encontrada"));
    }
}
