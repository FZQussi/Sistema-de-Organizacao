package com.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;

class RegistarEntradaServiceTest {

    private RegistarEntradaService service;

    @BeforeEach
    void setup() {
        service = new RegistarEntradaService();
    }

    // -----------------------------------------
    // TESTES MATRÍCULAS PORTUGUESAS
    // -----------------------------------------

    @Test
    void testMatriculasValidas() {
        assertTrue(service.matriculaValida("AA-12-34"));
        assertTrue(service.matriculaValida("12-34-AA"));
        assertTrue(service.matriculaValida("12-AA-34"));
        assertTrue(service.matriculaValida("AA-12-AA"));
    }

    @Test
    void testMatriculasValidas_Minusculas() {
        assertTrue(service.matriculaValida("aa-12-34"));
        assertTrue(service.matriculaValida("12-aa-34"));
        assertTrue(service.matriculaValida("12-34-aa"));
    }

    @Test
    void testMatriculasInvalidas() {
        assertFalse(service.matriculaValida("A1-12-34"));  // letra + número
        assertFalse(service.matriculaValida("AAA-12-34")); // 3 letras
        assertFalse(service.matriculaValida("AA-123-4"));  // grupos errados
        assertFalse(service.matriculaValida("AA12-34"));   // sem hífen
        assertFalse(service.matriculaValida("1234-AA"));   // formato errado
        assertFalse(service.matriculaValida(""));          // vazio
        assertFalse(service.matriculaValida("AA-AA-AA"));  // grupo num faltando
    }

    // -----------------------------------------
    // TESTES ANO DO CARRO
    // -----------------------------------------

    @Test
    void testAnoValido() {
        assertTrue(service.anoValido(1999));
        assertTrue(service.anoValido(1900));
        assertTrue(service.anoValido(Year.now().getValue())); // ano atual
    }

    @Test
    void testAnoInvalido() {
        assertFalse(service.anoValido(1800)); // demasiado antigo
        assertFalse(service.anoValido(3000)); // demasiado à frente
        assertFalse(service.anoValido(0));
        assertFalse(service.anoValido(-10));
    }

    @Test
    void testAnoLimiteSuperior() {
        int anoAtual = Year.now().getValue();
        assertTrue(service.anoValido(anoAtual));
        assertFalse(service.anoValido(anoAtual + 1));
    }

    @Test
    void testAnoLimiteInferior() {
        assertTrue(service.anoValido(1900));
        assertFalse(service.anoValido(1899));
    }
}
