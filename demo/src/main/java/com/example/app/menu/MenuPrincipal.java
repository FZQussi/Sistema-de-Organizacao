package com.example.app.menu;

import com.example.model.Utilizador;
import com.example.service.AuthService;
import com.example.service.GestaoEstacionamento;
import com.example.service.UserService;

import java.util.Scanner;

public class MenuPrincipal {

    private Scanner sc = new Scanner(System.in);
    private UserService userService = new UserService();
    private AuthService auth = new AuthService(userService);
    private GestaoEstacionamento gestao = new GestaoEstacionamento(10);

    public void iniciar() {

        System.out.println("===== SISTEMA DE GESTÃO =====");

        Utilizador loggedUser = fazerLogin();

        while (true) {
            System.out.println("\n===== MENU PRINCIPAL =====");

            if (loggedUser.getTipo().equals("gerente")) {
                System.out.println("1 - Gestão de Utilizadores");
                System.out.println("2 - Registrar entrada");
                System.out.println("3 - Registrar saída");
                System.out.println("4 - Listar carros");
                System.out.println("0 - Sair");

                System.out.print("Escolha: ");
                int escolha = Integer.parseInt(sc.nextLine());

                switch (escolha) {
                    case 1 -> new MenuGestaoUtilizadores(userService).mostrar();
                    case 2 -> registrarEntrada();
                    case 3 -> registrarSaida();
                    case 4 -> gestao.listarVagas();
                    case 0 -> {
                        auth.logout();
                        System.out.println("Sessão terminada.");
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            } else {
                System.out.println("1 - Registrar entrada");
                System.out.println("2 - Registrar saída");
                System.out.println("3 - Listar carros");
                System.out.println("0 - Sair");

                System.out.print("Escolha: ");
                int escolha = Integer.parseInt(sc.nextLine());

                switch (escolha) {
                    case 1 -> registrarEntrada();
                    case 2 -> registrarSaida();
                    case 3 -> gestao.listarVagas();
                    case 0 -> {
                        auth.logout();
                        System.out.println("Sessão terminada.");
                        return;
                    }
                    default -> System.out.println("Opção inválida!");
                }
            }
        }
    }

    private Utilizador fazerLogin() {
        Utilizador user;

        while (true) {
            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            user = auth.login(username, password);

            if (user != null) {
                System.out.println("Login efetuado: " + user.getUsername() +
                        " (" + user.getTipo() + ")");
                return user;
            }

            System.out.println("Login falhou. Tente novamente.");
        }
    }

    private void registrarEntrada() {
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
    }

    private void registrarSaida() {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        gestao.registrarSaida(placa);
    }
}
