package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuRemoverUtilizador {

    private static final Logger logger = LogManager.getLogger(MenuRemoverUtilizador.class);

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuRemoverUtilizador(UserService userService) {
        this.userService = userService;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║               REMOVER UTILIZADOR             ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {

        ConsoleUtils.clear();
        header();

        System.out.println(YELLOW + "Digite '0' para cancelar e voltar ao menu anterior.\n" + RESET);

        System.out.print(YELLOW + "→ Username do utilizador a remover: " + RESET);
        String username = sc.nextLine();

        if (username.equals("0")) {
            System.out.println(RED + "❌ Ação cancelada. Voltando ao menu anterior." + RESET);
            return;
        }

        Utilizador u = userService.getByUsername(username);

        if (u == null) {
            System.out.println(RED + "❌ Utilizador não encontrado." + RESET);
            logger.warn("Tentativa de remover utilizador inexistente: {}", username);
            return;
        }

        System.out.println(GREEN + "\nEncontrado:" + RESET);
        System.out.println(" - Nome: " + u.getNome() + " " + u.getSobrenome());
        System.out.println(" - Tipo: " + u.getTipo());

        System.out.print(YELLOW + "\nTem a certeza que deseja remover? (s/n ou 0 para cancelar): " + RESET);
        String confirm = sc.nextLine().toLowerCase();

        if (!confirm.equals("s")) {
            System.out.println(RED + "❌ Ação cancelada." + RESET);
            logger.info("Remocão cancelada pelo utilizador para '{}'", username);
            return;
        }

        userService.removeUser(username);
        System.out.println(GREEN + "✔ Utilizador removido com sucesso!" + RESET);
        logger.info("Utilizador removido: {}", username);
    }
}
