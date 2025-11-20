package com.example.app.menu;

import com.example.app.menu.login.MenuLogin;
import com.example.app.menu.utilizadores.MenuGestaoUtilizadores;

import com.example.app.menu.estacionamento.MenuListarCarros;
import com.example.app.menu.estacionamento.MenuRegistarEntrada;
import com.example.app.menu.estacionamento.MenuRegistarSaida;

import com.example.model.Utilizador;
import com.example.service.AuthService;
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
    private final AuthService auth = new AuthService(userService);
    private final GestaoEstacionamento gestao = new GestaoEstacionamento(10);

    public void iniciar() {

        ConsoleUtils.clear();
        System.out.println("===== SISTEMA DE GESTÃO =====");

        // 🔐 LOGIN
        Utilizador loggedUser = new MenuLogin(auth).mostrar();

        // 🔁 CICLO PRINCIPAL
        while (true) {
            ConsoleUtils.clear();
            System.out.println("\n===== MENU PRINCIPAL =====");

            if (loggedUser.getTipo().equals("gerente")) {
                System.out.println("1 - Gestão de Utilizadores");
                System.out.println("2 - Registrar entrada");
                System.out.println("3 - Registrar saída");
                System.out.println("4 - Listar carros");
                System.out.println("0 - Sair");
            } else {
                System.out.println("1 - Registrar entrada");
                System.out.println("2 - Registrar saída");
                System.out.println("3 - Listar carros");
                System.out.println("0 - Sair");
            }

            System.out.print("Escolha: ");
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
            return Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
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
                new MenuRegistarEntrada(gestao).mostrar();
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
                System.out.println("Opção inválida!");
                logger.warn("Opção inválida selecionada por gerente: {}", escolha);
            }
        }
    }

    private void handleOperador(Utilizador user, int escolha) {
        switch (escolha) {

            case 1 -> {
                logger.info("Operador '{}' acedeu ao registo de entradas.", user.getUsername());
                new MenuRegistarEntrada(gestao).mostrar();
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
                System.out.println("Opção inválida!");
                logger.warn("Opção inválida selecionada por operador: {}", escolha);
            }
        }
    }

    private void sair(Utilizador user) {
        auth.logout();
        logger.info("Usuário '{}' fez logout.", user.getUsername());
        System.out.println("Sessão terminada.\n");
        System.exit(0);
    }
}
