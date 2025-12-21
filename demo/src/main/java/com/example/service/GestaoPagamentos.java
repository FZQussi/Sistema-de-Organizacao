package com.example.service;

import com.example.model.MovimentoEstacionamento;
import com.example.utils.FileUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Serviço responsável pela gestão de pagamentos do estacionamento.
 * Regista os pagamentos efetuados em ficheiro.
 */
public class GestaoPagamentos {

    /**
     * Regista um pagamento associado a um movimento de estacionamento.
     * O registo é feito em modo de anexação (append) num ficheiro de pagamentos.
     *
     * @param placa   placa do veículo
     * @param entrada data e hora de entrada
     * @param saida   data e hora de saída
     * @param valor   valor pago pelo estacionamento
     */
    public static void registarPagamento(String placa,
                                         LocalDateTime entrada,
                                         LocalDateTime saida,
                                         double valor) {
        try (
                // Abre o ficheiro de pagamentos em modo append
                FileWriter fw = new FileWriter(FileUtils.getPagamentosFile(), true)
        ) {

            // Cria o objeto que representa o movimento de pagamento
            MovimentoEstacionamento mov =
                    new MovimentoEstacionamento(placa, entrada, saida, valor);

            // Escreve o movimento no ficheiro
            fw.write(mov.toString() + "\n");

        } catch (IOException e) {
            // Em caso de erro na escrita do ficheiro
            e.printStackTrace();
        }
    }
}
