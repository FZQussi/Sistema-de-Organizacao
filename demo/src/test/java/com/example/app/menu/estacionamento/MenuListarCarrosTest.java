package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class MenuListarCarrosTest {

    @Test
    void deveListarCarrosEVoltar() {
        GestaoEstacionamento gestaoMock = mock(GestaoEstacionamento.class);

        // Entrada simulada: escolher "0" para voltar
        Scanner sc = new Scanner("0\n");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        MenuListarCarros menu = new MenuListarCarros(gestaoMock, sc, out);

        menu.mostrar();

        // Verifica se chamou listarVagas()
        verify(gestaoMock, times(1)).listarVagas();

        // Verifica se o texto apareceu
        String printed = output.toString();

        assertTrue(printed.contains("Carros atualmente estacionados"));
        assertTrue(printed.contains("0 - Voltar"));
    }

    @Test
    void deveDetectarOpcaoInvalida() {
        GestaoEstacionamento gestaoMock = mock(GestaoEstacionamento.class);

        // Entrada: opção inválida -> depois 0 para sair
        Scanner sc = new Scanner("abc\n0\n");

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        MenuListarCarros menu = new MenuListarCarros(gestaoMock, sc, out);

        menu.mostrar();

        String printed = output.toString();

        assertTrue(printed.contains("✖ Opcão inválida!"));
        verify(gestaoMock, atLeastOnce()).listarVagas();
    }
}
