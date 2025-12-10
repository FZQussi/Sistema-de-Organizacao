package com.example.app.menu;

import com.example.app.menu.login.MenuLogin;
import com.example.app.menu.utilizadores.MenuGestaoUtilizadores;

import com.example.app.menu.estacionamento.MenuListarCarros;
import com.example.app.menu.estacionamento.MenuRegistarEntrada;
import com.example.app.menu.estacionamento.MenuRegistarSaida;

import com.example.model.Utilizador;
import com.example.service.AuthServicetest;
import com.example.service.GestaoEstacionamento;
import com.example.service.UserService;

import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuPrincipal {

    private static final Logger logger = LogManager.getLogger(MenuPrincipal.class);

    private final Scanner sc = new Scanner(System.in);
    private final UserService userService = new UserService();
    private final AuthServicetest auth = new AuthServicetest(userService);
    private final GestaoEstacionamento gestao = new GestaoEstacionamento(10);

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public void iniciar() {

        ConsoleUtils.clear();
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║              SISTEMA DE GESTÃO               ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);

        // 🔐 LOGIN
        Utilizador loggedUser = new MenuLogin(auth).mostrar();

        // 🔁 CICLO PRINCIPAL
        while (true) {
            ConsoleUtils.clear();
            System.out.println(CYAN + BOLD + "\n╔═══════════════════════════════════════════════╗");
            System.out.println("║                  MENU PRINCIPAL              ║");
            System.out.println("╚═══════════════════════════════════════════════╝" + RESET);

            if (loggedUser.getTipo().equals("gerente")) {
                System.out.println(YELLOW + "1" + RESET + " - Gestão de Utilizadores");
                System.out.println(YELLOW + "2" + RESET + " - Registrar entrada");
                System.out.println(YELLOW + "3" + RESET + " - Registrar saída");
                System.out.println(YELLOW + "4" + RESET + " - Listar carros");
                System.out.println(YELLOW + "0" + RESET + " - Sair");
            } else {
                System.out.println(YELLOW + "1" + RESET + " - Registrar entrada");
                System.out.println(YELLOW + "2" + RESET + " - Registrar saída");
                System.out.println(YELLOW + "3" + RESET + " - Listar carros");
                System.out.println(YELLOW + "0" + RESET + " - Sair");
            }

            System.out.print(CYAN + "→ Escolha: " + RESET);
            int escolha = lerOpcao();

            if (loggedUser.getTipo().equals("gerente")) {
                handleGerente(loggedUser, escolha);
            } else {
                handleOperador(loggedUser, escolha);
            }
        }
    }

    // ============================================
    //              MÉTODOS DE APOIO
    // ============================================

    private int lerOpcao() {
        try {
            return Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println(RED + "❌ Opcão inválida!" + RESET);
            logger.warn("Entrada inválida no menu principal.");
            return -1;
        }
    }

    private void handleGerente(Utilizador user, int escolha) {
        switch (escolha) {

            case 1 -> {
                logger.info("Gerente '{}' acedeu à gestão de utilizadores.", user.getUsername());
                new MenuGestaoUtilizadores(userService).mostrar();
            }

            case 2 -> {
                logger.info("Gerente '{}' acedeu ao registo de entradas.", user.getUsername());
                new MenuRegistarEntrada(gestao, sc, System.out).mostrar();

            }

            case 3 -> {
                logger.info("Gerente '{}' acedeu ao registo de saídas.", user.getUsername());
                new MenuRegistarSaida(gestao).mostrar();
            }

            case 4 -> {
                logger.info("Gerente '{}' listou carros.", user.getUsername());
                new MenuListarCarros(gestao).mostrar();
            }

            case 0 -> sair(user);

            default -> {
                System.out.println(RED + "❌ Opcão inválida!" + RESET);
                logger.warn("Opcão inválida selecionada por gerente: {}", escolha);
            }
        }
    }

    private void handleOperador(Utilizador user, int escolha) {
        switch (escolha) {

            case 1 -> {
                logger.info("Operador '{}' acedeu ao registo de entradas.", user.getUsername());
                new MenuRegistarEntrada(gestao, sc, System.out).mostrar();

            }

            case 2 -> {
                logger.info("Operador '{}' acedeu ao registo de saídas.", user.getUsername());
                new MenuRegistarSaida(gestao).mostrar();
            }

            case 3 -> {
                logger.info("Operador '{}' listou carros.", user.getUsername());
                new MenuListarCarros(gestao).mostrar();
            }

            case 0 -> sair(user);

            default -> {
                System.out.println(RED + "❌ Opcão inválida!" + RESET);
                logger.warn("Opcão inválida selecionada por operador: {}", escolha);
            }
        }
    }

    private void sair(Utilizador user) {
        auth.logout();
        logger.info("Usuário '{}' fez logout.", user.getUsername());
        System.out.println(GREEN + "\n✔ Sessão terminada." + RESET);
        System.exit(0);
    }
}
