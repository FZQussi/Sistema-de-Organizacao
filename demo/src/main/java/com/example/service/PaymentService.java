package com.example.service;

import com.example.model.Utilizador;
import com.example.utils.FileUtils;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.YearMonth;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentService {

    private static final Logger logger = LogManager.getLogger(PaymentService.class);

    public void calcularPagamentoMensal(Utilizador u, int ano, int mes) {
        logger.info("=== Início cálculo pagamento para {} em {}/{} ===", u.getUsername(), mes, ano);

        double salarioHora = u.getSalario();
        long totalMinutos = calcularMinutosTrabalhados(u.getUsername(), ano, mes);
        long minutosAtraso = calcularAtrasos(u.getUsername(), ano, mes);

        double horasTrabalhadas = totalMinutos / 60.0;
        double horasAtraso = minutosAtraso / 60.0;

        double pagamentoBruto = horasTrabalhadas * salarioHora;
        double descontoAtrasos = horasAtraso * salarioHora;
        double pagamentoFinal = pagamentoBruto - descontoAtrasos;

        gravarPagamento(u.getUsername(), ano, mes, pagamentoBruto, descontoAtrasos, pagamentoFinal);

        // --- OUTPUT NA CONSOLA ---
        System.out.println("\n=== Pagamento Mensal ===");
        System.out.println("Utilizador:       " + u.getUsername());
        System.out.println("Horas trabalhadas: " + horasTrabalhadas);
        System.out.println("Horas de atraso:   " + horasAtraso);
        System.out.println("Salário por hora:  " + salarioHora);
        System.out.println("-------------------------------");
        System.out.println("Bruto:             " + pagamentoBruto + "€");
        System.out.println("Desconto atrasos:  -" + descontoAtrasos + "€");
        System.out.println("TOTAL A PAGAR:     " + pagamentoFinal + "€");

        logger.info("Pagamento calculado: Bruto={}€, Desconto={}€, Total={}€",
                pagamentoBruto, descontoAtrasos, pagamentoFinal);
    }

    // -------------------------------
    //       CALCULAR MINUTOS TRABALHADOS
    // -------------------------------
    private long calcularMinutosTrabalhados(String username, int ano, int mes) {
        try {
            List<String> linhas = Files.readAllLines(FileUtils.getTurnosFile().toPath());
            long total = 0;
            boolean blocoDoUser = false;

            for (String linha : linhas) {
                // Detecta início de bloco do utilizador para o mês/ano correto
                if (linha.startsWith("[")) {
                    blocoDoUser = linha.contains(username)
                            && linha.startsWith("[" + ano + "-" + String.format("%02d", mes));
                }

                if (blocoDoUser && linha.contains("Total trabalhado:")) {
                    String temp = linha.replace("Total trabalhado:", "").trim(); // ex: "-  0h 0min"
                    Pattern p = Pattern.compile("(\\d+)h\\s*(\\d+)min");
                    Matcher m = p.matcher(temp);
                    if (m.find()) {
                        long horas = Long.parseLong(m.group(1));
                        long mins = Long.parseLong(m.group(2));
                        total += horas * 60 + mins;
                        logger.debug("Adicionando {}h {}min => total minutos: {}", horas, mins, total);
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

    // -------------------------------
    //       CALCULAR ATRASOS
    // -------------------------------
    private long calcularAtrasos(String username, int ano, int mes) {
        try {
            List<String> linhas = Files.readAllLines(FileUtils.getTurnosFile().toPath());
            long total = 0;
            boolean blocoDoUser = false;

            for (String linha : linhas) {
                if (linha.startsWith("[")) {
                    blocoDoUser = linha.contains(username)
                            && linha.startsWith("[" + ano + "-" + String.format("%02d", mes));
                }

                if (blocoDoUser && linha.contains("Atraso")) {
                    // Extrai apenas números da linha
                    Pattern p = Pattern.compile("(\\d+)");
                    Matcher m = p.matcher(linha);
                    if (m.find()) {
                        long atraso = Long.parseLong(m.group(1));
                        total += atraso;
                        logger.debug("Atraso encontrado: {} min => total: {}", atraso, total);
                    }
                }
            }
            return total;

        } catch (IOException e) {
            logger.error("Erro ao calcular atrasos", e);
            return 0;
        }
    }

    // -------------------------------
    //       GUARDAR NO FICHEIRO
    // -------------------------------
    private void gravarPagamento(String username, int ano, int mes,
                                 double bruto, double desconto, double finalP) {
        try (FileWriter fw = new FileWriter(FileUtils.getPagamentosFile(), true)) {
            fw.write(String.format("[%04d-%02d] %s | Bruto: %.2f€ | Desconto: -%.2f€ | Final: %.2f€\n",
                    ano, mes, username, bruto, desconto, finalP));
            logger.info("Pagamento gravado no ficheiro para {}: Final={}€", username, finalP);
        } catch (IOException e) {
            logger.error("Erro ao gravar pagamento", e);
        }
    }
}
