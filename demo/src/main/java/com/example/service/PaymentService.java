package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Serviço responsável pelo cálculo do pagamento mensal dos utilizadores.
 * O cálculo é baseado nos turnos registados e nos atrasos acumulados.
 */
public class PaymentService {

    // Logger para auditoria e diagnóstico
    private static final Logger logger = LogManager.getLogger(PaymentService.class);

    /**
     * Calcula o pagamento mensal de um utilizador para um determinado mês e ano.
     *
     * @param u   utilizador a calcular o pagamento
     * @param ano ano de referência
     * @param mes mês de referência
     */
    public void calcularPagamentoMensal(Utilizador u, int ano, int mes) {

        logger.info("=== Início cálculo pagamento para {} em {}/{} ===",
                u.getUsername(), mes, ano);

        // Salário por hora do utilizador
        double salarioHora = u.getSalario();

        // Total de minutos trabalhados no mês
        long totalMinutos = calcularMinutosTrabalhados(u.getUsername(), ano, mes);

        // Total de minutos de atraso no mês
        long minutosAtraso = calcularAtrasos(u.getUsername(), ano, mes);

        // Conversão para horas
        double horasTrabalhadas = totalMinutos / 60.0;
        double horasAtraso = minutosAtraso / 60.0;

        // Cálculos financeiros
        double pagamentoBruto = horasTrabalhadas * salarioHora;
        double descontoAtrasos = horasAtraso * salarioHora;
        double pagamentoFinal = pagamentoBruto - descontoAtrasos;

        // Grava o pagamento no ficheiro
        gravarPagamento(u.getUsername(), ano, mes,
                pagamentoBruto, descontoAtrasos, pagamentoFinal);

        // -------- OUTPUT NA CONSOLA --------
        System.out.println("\n=== Pagamento Mensal ===");
        System.out.println("Utilizador:        " + u.getUsername());
        System.out.println("Horas trabalhadas: " + horasTrabalhadas);
        System.out.println("Horas de atraso:   " + horasAtraso);
        System.out.println("Salário por hora:  " + salarioHora);
        System.out.println("-------------------------------");
        System.out.println("Bruto:             " + pagamentoBruto + " Eur");
        System.out.println("Desconto atrasos:  -" + descontoAtrasos + " Eur");
        System.out.println("TOTAL A PAGAR:     " + pagamentoFinal + " Eur");

        logger.info("Pagamento calculado: Bruto={}Eur, Desconto={}Eur, Total={}Eur",
                pagamentoBruto, descontoAtrasos, pagamentoFinal);
    }

    // =====================================================
    // CÁLCULO DO TOTAL DE MINUTOS TRABALHADOS
    // =====================================================
    private long calcularMinutosTrabalhados(String username, int ano, int mes) {
        try {
            // Lê todas as linhas do ficheiro de turnos
            List<String> linhas = Files.readAllLines(FileUtils.getTurnosFile().toPath());

            long total = 0;
            boolean blocoDoUser = false;

            for (String linha : linhas) {

                // Identifica o início do bloco do utilizador no mês/ano corretos
                if (linha.startsWith("[")) {
                    blocoDoUser = linha.contains(username)
                            && linha.startsWith("[" + ano + "-" + String.format("%02d", mes));
                }

                // Extrai o total trabalhado dentro do bloco
                if (blocoDoUser && linha.contains("Total trabalhado:")) {

                    // Exemplo esperado: "Total trabalhado: 8h 30min"
                    String temp = linha.replace("Total trabalhado:", "").trim();

                    Pattern p = Pattern.compile("(\\d+)h\\s*(\\d+)min");
                    Matcher m = p.matcher(temp);

                    if (m.find()) {
                        long horas = Long.parseLong(m.group(1));
                        long mins = Long.parseLong(m.group(2));
                        total += horas * 60 + mins;

                        logger.debug("Adicionando {}h {}min => total minutos: {}",
                                horas, mins, total);
                    } else {
                        logger.warn("Não foi possível extrair horas/minutos de '{}'", temp);
                    }
                }
            }
            return total;

        } catch (IOException e) {
            logger.error("Erro ao calcular minutos trabalhados", e);
            return 0;
        }
    }

    // =====================================================
    // CÁLCULO DOS ATRASOS
    // =====================================================
    private long calcularAtrasos(String username, int ano, int mes) {
        try {
            List<String> linhas = Files.readAllLines(FileUtils.getTurnosFile().toPath());

            long total = 0;
            boolean blocoDoUser = false;

            for (String linha : linhas) {

                // Identifica o bloco correto do utilizador
                if (linha.startsWith("[")) {
                    blocoDoUser = linha.contains(username)
                            && linha.startsWith("[" + ano + "-" + String.format("%02d", mes));
                }

                // Procura linhas que contenham atrasos
                if (blocoDoUser && linha.contains("Atraso")) {

                    // Extrai apenas o valor numérico do atraso (em minutos)
                    Pattern p = Pattern.compile("(\\d+)");
                    Matcher m = p.matcher(linha);

                    if (m.find()) {
                        long atraso = Long.parseLong(m.group(1));
                        total += atraso;

                        logger.debug("Atraso encontrado: {} min => total: {}",
                                atraso, total);
                    }
                }
            }
            return total;

        } catch (IOException e) {
            logger.error("Erro ao calcular atrasos", e);
            return 0;
        }
    }

    // =====================================================
    // GRAVAÇÃO DO PAGAMENTO NO FICHEIRO
    // =====================================================
    private void gravarPagamento(String username, int ano, int mes,
                                 double bruto, double desconto, double finalP) {
        try (FileWriter fw = new FileWriter(FileUtils.getPagamentosFile(), true)) {

            // Registo formatado do pagamento mensal
            fw.write(String.format(
                    "[%04d-%02d] %s | Bruto: %.2f€ | Desconto: -%.2f€ | Final: %.2f€\n",
                    ano, mes, username, bruto, desconto, finalP));

            logger.info("Pagamento gravado no ficheiro para {}: Final={}€",
                    username, finalP);

        } catch (IOException e) {
            logger.error("Erro ao gravar pagamento", e);
        }
    }
}
