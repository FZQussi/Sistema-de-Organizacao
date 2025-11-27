package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuListarCarros {

    private static final Logger logger = LogManager.getLogger(MenuListarCarros.class);

    private final GestaoEstacionamento gestao;
    private final Scanner sc;
    private final PrintStream out;

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    // Construtor testável
    public MenuListarCarros(GestaoEstacionamento gestao, Scanner sc, PrintStream out) {
        this.gestao = gestao;
        this.sc = sc;
        this.out = out;
    }

    // Construtor normal
    public MenuListarCarros(GestaoEstacionamento gestao) {
        this(gestao, new Scanner(System.in), System.out);
    }

    private void header() {
        out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════════╗");
        out.println("║         LISTAGEM DE CARROS NO ESTACIONAMENTO      ║");
        out.println("╚═══════════════════════════════════════════════════╝" + RESET);
        out.println();
    }

    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();
            header();

            out.println(GREEN + BOLD + "Carros atualmente estacionados:\n" + RESET);

            gestao.listarVagas(); // Vamos mockar isto nos testes

            out.println(CYAN + "\n────────────────────────────────────────────────────" + RESET);
            out.println(YELLOW + "0 - Voltar" + RESET);
            out.print(YELLOW + "→ Escolha: " + RESET);

            String opcao = sc.nextLine().trim();

            if (opcao.equals("0")) {
                logger.info("Voltando ao menu principal a partir do MenuListarCarros.");
                return;
            }

            out.println(RED + BOLD + "\n✖ Opcão inválida! Tente novamente." + RESET);
            out.println(CYAN + "────────────────────────────────────────────────────\n" + RESET);
        }
    }
}
