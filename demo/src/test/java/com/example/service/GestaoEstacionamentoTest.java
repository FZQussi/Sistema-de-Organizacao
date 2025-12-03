package com.example.service;

import com.example.model.Carro;
import com.example.model.Estacionamento;
import com.example.utils.MovimentosUtils;

import org.junit.jupiter.api.*;
import org.mockito.MockedStatic;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GestaoEstacionamentoTest {

    private GestaoEstacionamento gestao;

    @BeforeEach
    void setup() {
        gestao = new GestaoEstacionamento(2); // capacidade pequena para testar limite
    }

    @Test
    void testRegistrarEntradaComSucesso() {

        try (MockedStatic<MovimentosUtils> mocked = mockStatic(MovimentosUtils.class)) {

            boolean result = gestao.registrarEntrada("AA-11-BB", "BMW", "320i", "Preto", 2020);

            assertTrue(result);
            mocked.verify(() -> MovimentosUtils.registarEntrada(eq("AA-11-BB"), any()), times(1));
        }
    }

    @Test
    void testRegistrarEntradaFalhaPorCapacidade() {

        gestao.registrarEntrada("AA-11-BB", "BMW", "320i", "Preto", 2020);
        gestao.registrarEntrada("CC-22-DD", "Audi", "A4", "Branco", 2021);

        // 3ª entrada já não cabe
        boolean result = gestao.registrarEntrada("EE-33-FF", "Mercedes", "C200", "Preto", 2019);

        assertFalse(result);
    }

    @Test
    void testRegistrarSaidaComSucesso() {

        // Primeira entrada
        gestao.registrarEntrada("AA-11-BB", "BMW", "320i", "Preto", 2020);

        // MOCKAR estáticos
        try (MockedStatic<MovimentosUtils> mocked = mockStatic(MovimentosUtils.class)) {

            boolean result = gestao.registrarSaida("AA-11-BB");

            assertTrue(result);

            mocked.verify(() ->
                    MovimentosUtils.registarSaida(
                            eq("AA-11-BB"),
                            any(LocalDateTime.class),
                            any(LocalDateTime.class),
                            anyDouble()
                    ), times(1));
        }
    }

    @Test
    void testRegistrarSaidaCarroNaoExiste() {

        boolean result = gestao.registrarSaida("XX-00-YY");

        assertFalse(result);
    }

    @Test
    void testCalculoPrecoCorreto() {

        // Entrada
        gestao.registrarEntrada("AA-11-BB", "BMW", "320i", "Preto", 2020);

        // Pegamos o carro internamente para simular tempo
        Estacionamento est = new Estacionamento(5);
        Carro c = new Carro("AA-11-BB", "BMW", "320i", "Preto", 2020);
        c.setEntrada(LocalDateTime.now().minusHours(2)); // simulamos 2 horas
        est.entrada(c);

        // Agora testamos o método de saída (mockando MovimentosUtils)
        try (MockedStatic<MovimentosUtils> mocked = mockStatic(MovimentosUtils.class)) {

            gestao.registrarSaida("AA-11-BB");

            // preço é 1.50 por hora → 2h = 3.00€
            mocked.verify(() ->
                    MovimentosUtils.registarSaida(
                            eq("AA-11-BB"),
                            any(),
                            any(),
                            argThat(v -> v >= 2.90 && v <= 3.10) // tolerância
                    )
            );
        }
    }
}
