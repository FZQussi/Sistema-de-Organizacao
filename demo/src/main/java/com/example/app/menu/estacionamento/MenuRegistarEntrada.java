package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.Year;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MenuRegistarEntrada {

    private static final Logger logger = LogManager.getLogger(MenuRegistarEntrada.class);

    private static final Pattern MATRICULA_PT = Pattern.compile(
            "^[A-Z]{2}-\\d{2}-\\d{2}$"      // AA-00-00
            + "|^\\d{2}-\\d{2}-[A-Z]{2}$"   // 00-00-AA
            + "|^\\d{2}-[A-Z]{2}-\\d{2}$"   // 00-AA-00
            + "|^[A-Z]{2}-\\d{2}-[A-Z]{2}$" // AA-00-AA
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

    public MenuRegistarEntrada(GestaoEstacionamento gestao) {
        this.gestao = gestao;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════════╗");
        System.out.println("║           REGISTAR ENTRADA DE CARRO          ║");
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

    private String pedirTexto(String label) {
        while (true) {
            System.out.print(YELLOW + "→ " + label + ": " + RESET);
            String input = sc.nextLine().trim();
            if (!input.isEmpty()) return input;

            System.out.println(RED + BOLD + "✖ Este campo não pode ficar vazio!\n" + RESET);
        }
    }

    private int pedirAno() {
        int anoAtual = Year.now().getValue();

        while (true) {
            System.out.print(YELLOW + "→ Ano: " + RESET);
            try {
                int ano = Integer.parseInt(sc.nextLine().trim());
                if (ano >= 1900 && ano <= anoAtual) return ano;

                System.out.println(RED + BOLD + "✖ Ano inválido! Deve estar entre 1900 e " + anoAtual + ".\n" + RESET);
            } catch (NumberFormatException e) {
                System.out.println(RED + BOLD + "✖ Ano inválido! Digite apenas números.\n" + RESET);
            }
        }
    }

    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();
            header();

            String placa = pedirMatricula();
            if (placa.equals("0")) return;

            String marca = pedirTexto("Marca");
            String modelo = pedirTexto("Modelo");
            String cor = pedirTexto("Cor");
            int ano = pedirAno();

            boolean sucesso = gestao.registrarEntrada(placa, marca, modelo, cor, ano);

            System.out.println(CYAN + "──────────────────────────────────────────────" + RESET);

            if (sucesso) {
                System.out.println(GREEN + BOLD + "✔ Carro registado com sucesso!" + RESET);
                logger.info("Entrada registada: {} {} {} {} {}", placa, marca, modelo, cor, ano);
            } else {
                System.out.println(RED + BOLD + "✖ Falha: Estacionamento cheio ou placa já registada!" + RESET);
                logger.warn("Falha ao registar entrada para placa: {}", placa);
            }

            System.out.println(CYAN + "──────────────────────────────────────────────\n" + RESET);
            System.out.println(YELLOW + "1 - Registar outra entrada" + RESET);
            System.out.println(YELLOW + "0 - Voltar" + RESET);
            System.out.print(YELLOW + "→ Escolha: " + RESET);

            if (sc.nextLine().trim().equals("0")) return;
        }
    }
}
