package com.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Classe utilitária para registo de movimentos de veículos
 * no estacionamento, gravando entradas e saídas no ficheiro movimentos.txt.
 */
public class MovimentosUtils {

    /**
     * Regista a entrada de um carro no estacionamento.
     *
     * @param placa   matrícula do carro
     * @param entrada data e hora de entrada
     */
    public static void registarEntrada(String placa, LocalDateTime entrada) {
        escrever("ENTRADA | " + placa + " | " + entrada + "\n");
    }

    /**
     * Regista a saída de um carro do estacionamento,
     * incluindo o valor pago.
     *
     * @param placa   matrícula do carro
     * @param entrada data e hora de entrada
     * @param saida   data e hora de saída
     * @param valor   valor pago pelo estacionamento
     */
    public static void registarSaida(String placa, LocalDateTime entrada, LocalDateTime saida, double valor) {
        escrever("SAIDA   | " + placa + " | Entrada: " + entrada +
                 " | Saida: " + saida + " | Pago: " + valor + "€\n");
    }

    /**
     * Escreve a linha de registo no ficheiro movimentos.txt.
     * Utiliza FileWriter em modo append para adicionar novas entradas.
     *
     * @param linha texto a gravar
     */
    private static void escrever(String linha) {
        try (FileWriter fw = new FileWriter(FileUtils.getMovimentosFile(), true)) {
            fw.write(linha);
        } catch (IOException e) {
            // Em caso de erro de I/O, imprime stack trace
            e.printStackTrace();
        }
    }
}
