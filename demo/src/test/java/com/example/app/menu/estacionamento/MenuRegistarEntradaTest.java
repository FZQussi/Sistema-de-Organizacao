package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;

class MenuRegistarEntradaTest {

    @Test
    void testPedirMatriculaValida() {
        Scanner sc = new Scanner("AA-11-22\n");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        GestaoEstacionamento gestao = Mockito.mock(GestaoEstacionamento.class);
        MenuRegistarEntrada menu = new MenuRegistarEntrada(gestao, sc, out);

        String result = menu.pedirMatricula();
        assertEquals("AA-11-22", result);
    }

    @Test
    void testPedirMatriculaInvalidaDepoisValida() {
        Scanner sc = new Scanner("XXX\nAA-33-44\n");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        GestaoEstacionamento gestao = Mockito.mock(GestaoEstacionamento.class);
        MenuRegistarEntrada menu = new MenuRegistarEntrada(gestao, sc, out);

        String result = menu.pedirMatricula();

        assertEquals("AA-33-44", result);
        assertTrue(output.toString().contains("Matrícula inválida"));
    }

    @Test
    void testPedirAno() {
        Scanner sc = new Scanner("1800\nabc\n3000\n2012\n");
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        GestaoEstacionamento gestao = Mockito.mock(GestaoEstacionamento.class);
        MenuRegistarEntrada menu = new MenuRegistarEntrada(gestao, sc, out);

        int ano = menu.pedirAno();
        assertEquals(2012, ano);
    }

    @Test
    void testMostrarFluxoCompleto() {
        Scanner sc = new Scanner(
                "AA-10-20\nToyota\nCorolla\nPreto\n2010\n0\n"
        );

        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(output);

        GestaoEstacionamento gestao = Mockito.mock(GestaoEstacionamento.class);
        Mockito.when(gestao.registrarEntrada(
                Mockito.any(), Mockito.any(), Mockito.any(), Mockito.any(), Mockito.anyInt()
        )).thenReturn(true);

        MenuRegistarEntrada menu = new MenuRegistarEntrada(gestao, sc, out);
        menu.mostrar();

        assertTrue(output.toString().contains("Carro registado com sucesso"));
    }
}
