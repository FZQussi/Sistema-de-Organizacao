package com.example.service;

import com.example.model.MovimentoEstacionamento;
import com.example.utils.FileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

public class GestaoPagamentos {

    public static void registarPagamento(String placa, LocalDateTime entrada, LocalDateTime saida, double valor) {
        try (FileWriter fw = new FileWriter(FileUtils.getPagamentosFile(), true)) {
            MovimentoEstacionamento mov = new MovimentoEstacionamento(placa, entrada, saida, valor);
            fw.write(mov.toString() + "\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
