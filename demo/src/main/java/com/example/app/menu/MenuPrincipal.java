package com.example.app.menu;

import com.example.model.Utilizador;
import com.example.service.AuthService;
import com.example.service.GestaoEstacionamento;
import com.example.service.UserService;
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
    System.out.println("===== SISTEMA DE GESTÃO =====");
    Utilizador loggedUser = fazerLogin();

    while (true) {
        System.out.println("\n===== MENU PRINCIPAL =====");

        if (loggedUser.getTipo().equals("gerente")) {
            System.out.println("1 - Gestao de Utilizadores");
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
        int escolha = -1;
        try {
            escolha = Integer.parseInt(sc.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Por favor, digite um número válido!");
            logger.warn("Entrada inválida (não é número): {}", e.getMessage());
            continue; // volta para o menu
        }

        if (loggedUser.getTipo().equals("gerente")) {
            switch (escolha) {
                case 1 -> {
                    logger.info("Gerente '{}' entrou no menu de gestão de utilizadores.", loggedUser.getUsername());
                    new MenuGestaoUtilizadores(userService).mostrar();
                }
                case 2 -> registrarEntrada(loggedUser);
                case 3 -> registrarSaida(loggedUser);
                case 4 -> {
                    gestao.listarVagas();
                    logger.info("Gerente '{}' listou carros.", loggedUser.getUsername());
                }
                case 0 -> {
                    auth.logout();
                    logger.info("Usuário '{}' efetuou logout.", loggedUser.getUsername());
                    System.out.println("Sessão terminada.");
                    return;
                }
                default -> {
                    System.out.println("Opção inválida! Tente novamente.");
                    logger.warn("Opção inválida no menu principal (gerente): {}", escolha);
                }
            }
        } else {
            switch (escolha) {
                case 1 -> registrarEntrada(loggedUser);
                case 2 -> registrarSaida(loggedUser);
                case 3 -> {
                    gestao.listarVagas();
                    logger.info("Operador '{}' listou carros.", loggedUser.getUsername());
                }
                case 0 -> {
                    auth.logout();
                    logger.info("Usuário '{}' efetuou logout.", loggedUser.getUsername());
                    System.out.println("Sessão terminada.");
                    return;
                }
                default -> {
                    System.out.println("Opção inválida! Tente novamente.");
                    logger.warn("Opção inválida no menu principal (operador): {}", escolha);
                }
            }
        }
    }
}


    private Utilizador fazerLogin() {
        Utilizador user;

        while (true) {
            System.out.print("Username: ");
            String username = sc.nextLine().trim();

            System.out.print("Password: ");
            String password = sc.nextLine();

            user = auth.login(username, password);

            if (user != null) {
                System.out.println("Login efetuado: " + user.getNome() + " " + user.getSobrenome() +
                                   " (" + user.getTipo() + ")");
                logger.info("Login bem-sucedido: {} ({})", username, user.getTipo());
                return user;
            }

            System.out.println("Login falhou. Username ou password incorretos. Tente novamente.\n");
            logger.warn("Falha de login para username: {}", username);
        }
    }

    private void registrarEntrada(Utilizador user) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        System.out.print("Marca: ");
        String marca = sc.nextLine();
        System.out.print("Modelo: ");
        String modelo = sc.nextLine();
        System.out.print("Cor: ");
        String cor = sc.nextLine();
        System.out.print("Ano: ");
        int ano = Integer.parseInt(sc.nextLine());

        gestao.registrarEntrada(placa, marca, modelo, cor, ano);
        logger.info("Usuário '{}' registrou entrada do carro: {} {} {} {} {}", user.getUsername(), placa, marca, modelo, cor, ano);
    }

    private void registrarSaida(Utilizador user) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        gestao.registrarSaida(placa);
        logger.info("Usuário '{}' registrou saída do carro: {}", user.getUsername(), placa);
    }
}
