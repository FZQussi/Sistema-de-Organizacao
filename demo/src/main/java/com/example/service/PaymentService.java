package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentService.class);

    public void calcularPagamentoMensal(Utilizador u, int ano, int mes) {

        long minutosTrabalhados = calcularMinutosTrabalhados(u.getUsername(), ano, mes);
        long minutosAtraso = calcularAtrasos(u.getUsername(), ano, mes);

        double salarioHora = u.getSalario();
        double horasTrabalhadas = minutosTrabalhados / 60.0;
        double desconto = (minutosAtraso / 60.0) * salarioHora;

        double pagamentoBruto = horasTrabalhadas * salarioHora;
        double pagamentoFinal = pagamentoBruto - desconto;

        gravarPagamento(u.getUsername(), ano, mes, pagamentoFinal);

        System.out.println("\n=== Pagamento Mensal ===");
        System.out.println("Utilizador: " + u.getUsername());
        System.out.println("Horas trabalhadas: " + horasTrabalhadas);
        System.out.println("Minutos atraso: " + minutosAtraso);
        System.out.println("Salário/hora: " + salarioHora);
        System.out.println("Pagamento final: " + pagamentoFinal + "€");
    }


    // ----------------------------------------------------
    //          PROCESSAMENTO DE TURNOS POR BLOCOS
    // ----------------------------------------------------

    private List<List<String>> lerBlocosTurnos() throws IOException {
        List<String> linhas = Files.readAllLines(FileUtils.getTurnosFile().toPath());
        List<List<String>> blocos = new ArrayList<>();

        List<String> atual = new ArrayList<>();
        for (String linha : linhas) {
            if (linha.startsWith("[") && linha.contains("]")) {
                if (!atual.isEmpty()) {
                    blocos.add(new ArrayList<>(atual));
                    atual.clear();
                }
            }
            atual.add(linha);

            if (linha.contains("-----------------------------------------")) {
                blocos.add(new ArrayList<>(atual));
                atual.clear();
            }
        }

        return blocos;
    }

    private long calcularMinutosTrabalhados(String username, int ano, int mes) {
        try {
            List<List<String>> blocos = lerBlocosTurnos();
            long total = 0;

            for (List<String> bloco : blocos) {

                if (!bloco.get(0).contains(username)) continue;

                // Extrair data do bloco
                String header = bloco.get(0);
                String dataStr = header.substring(header.indexOf("[") + 1, header.indexOf("]"));
                LocalDate data = LocalDate.parse(dataStr);

                if (data.getYear() != ano || data.getMonthValue() != mes) continue;

                for (String linha : bloco) {
                    if (linha.contains("Total trabalhado")) {
                        String tempo = linha.split(":")[1].trim(); // "0h 10min"
                        String[] partes = tempo.split("[hmin ]+"); // divide por letras

                        long horas = Long.parseLong(partes[0]);
                        long minutos = Long.parseLong(partes[1]);

                        total += horas * 60 + minutos;
                    }
                }
            }

            return total;

        } catch (Exception e) {
            logger.error("Erro ao calcular minutos trabalhados", e);
            return 0;
        }
    }

    private long calcularAtrasos(String username, int ano, int mes) {
        try {
            List<List<String>> blocos = lerBlocosTurnos();
            long total = 0;

            for (List<String> bloco : blocos) {

                if (!bloco.get(0).contains(username)) continue;

                String header = bloco.get(0);
                String dataStr = header.substring(header.indexOf("[") + 1, header.indexOf("]"));
                LocalDate data = LocalDate.parse(dataStr);

                if (data.getYear() != ano || data.getMonthValue() != mes) continue;

                for (String linha : bloco) {
                    if (linha.contains("Atraso:")) {
                        String min = linha.split(":")[2].replace("minutos", "").trim();
                        total += Long.parseLong(min);
                    }
                }
            }

            return total;

        } catch (Exception e) {
            logger.error("Erro ao calcular atrasos", e);
            return 0;
        }
    }

    private void gravarPagamento(String username, int ano, int mes, double valor) {
        try (FileWriter fw = new FileWriter(FileUtils.getPagamentosFile(), true)) {
            fw.write("[" + ano + "-" + mes + "] " + username + " -> " + valor + "€\n");
        } catch (IOException e) {
            logger.error("Erro ao gravar pagamento", e);
        }
    }
}
