package com.example.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class MovimentosUtils {

    public static void registarEntrada(String placa, LocalDateTime entrada) {
        escrever("ENTRADA | " + placa + " | " + entrada + "\n");
    }

    public static void registarSaida(String placa, LocalDateTime entrada, LocalDateTime saida, double valor) {
        escrever("SAIDA   | " + placa + " | Entrada: " + entrada +
                 " | Saida: " + saida + " | Pago: " + valor + "€\n");
    }

    private static void escrever(String linha) {
        try (FileWriter fw = new FileWriter(FileUtils.getMovimentosFile(), true)) {
            fw.write(linha);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
