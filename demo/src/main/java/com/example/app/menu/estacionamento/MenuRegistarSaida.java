package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;
import java.util.regex.Pattern;

public class MenuRegistarSaida {

    private static final Logger logger = LogManager.getLogger(MenuRegistarSaida.class);

    private static final Pattern MATRICULA_PT = Pattern.compile(
            "^[A-Z]{2}-\\d{2}-\\d{2}$"      
            + "|^\\d{2}-\\d{2}-[A-Z]{2}$"   
            + "|^\\d{2}-[A-Z]{2}-\\d{2}$"   
            + "|^[A-Z]{2}-\\d{2}-[A-Z]{2}$"
    );

    private final GestaoEstacionamento gestao;
    private final Scanner sc = new Scanner(System.in);

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuRegistarSaida(GestaoEstacionamento gestao) {
        this.gestao = gestao;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════════╗");
        System.out.println("║            REGISTAR SAÍDA DE CARRO           ║");
        System.out.println("╚══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    private String pedirMatricula() {
        while (true) {
            System.out.print(YELLOW + "→ Placa (0 para voltar): " + RESET);
            String placa = sc.nextLine().trim().toUpperCase();

            if (placa.equals("0")) return "0";

            if (!MATRICULA_PT.matcher(placa).matches()) {
                System.out.println(RED + BOLD + "✖ Matrícula inválida! Formatos aceites: AA-00-00 / 00-AA-00 / 00-00-AA / AA-00-AA.\n" + RESET);
                continue;
            }

            return placa;
        }
    }

    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();
            header();

            String placa = pedirMatricula();
            if (placa.equals("0")) return;

            boolean sucesso = gestao.registrarSaida(placa);

            System.out.println(CYAN + "──────────────────────────────────────────────" + RESET);

            if (sucesso) {
                System.out.println(GREEN + BOLD + "✔ Saída registada com sucesso!" + RESET);
                logger.info("Saída registada para a placa: {}", placa);
            } else {
                System.out.println(RED + BOLD + "✖ Falha: Placa não encontrada no estacionamento!" + RESET);
                logger.warn("Falha ao registar saída para a placa: {}", placa);
            }

            System.out.println(CYAN + "──────────────────────────────────────────────\n" + RESET);

            System.out.println(YELLOW + "1 - Registar outra saída" + RESET);
            System.out.println(YELLOW + "0 - Voltar" + RESET);
            System.out.print(YELLOW + "→ Escolha: " + RESET);

            if (sc.nextLine().trim().equals("0")) return;
        }
    }
}
