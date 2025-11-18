package com.example.app;

import com.example.model.Utilizador;
import com.example.service.AuthService;
import com.example.service.GestaoEstacionamento;
import com.example.service.UserService;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        UserService userService = new UserService();
        AuthService auth = new AuthService(userService);

        System.out.println("===== SISTEMA DE GESTÃO =====");

        // LOGIN PRIMEIRO
        Utilizador loggedUser;
        while (true) {
            System.out.print("Username: ");
            String username = sc.nextLine();

            System.out.print("Password: ");
            String password = sc.nextLine();

            loggedUser = auth.login(username, password);
            if (loggedUser != null) {
                System.out.println("Login efetuado: " + loggedUser.getUsername() +
                                   " (" + loggedUser.getTipo() + ")");
                break;
            } else {
                System.out.println("Login falhou. Tente novamente.");
            }
        }

        // Menu principal
        GestaoEstacionamento gestao = new GestaoEstacionamento(10);

        while (true) {
            System.out.println("\n===== MENU PRINCIPAL =====");

            if (loggedUser.getTipo().equals("gerente")) {
                System.out.println("GERENTE: Menu especial");
                System.out.println("1 - Criar novo operador");
                System.out.println("2 - Listar utilizadores");
                System.out.println("3 - Registrar entrada");
                System.out.println("4 - Registrar saída");
                System.out.println("5 - Listar carros");
                System.out.println("0 - Sair");
            } else {
                System.out.println("1 - Registrar entrada");
                System.out.println("2 - Registrar saída");
                System.out.println("3 - Listar carros");
                System.out.println("0 - Sair");
            }

            System.out.print("Escolha: ");
            int escolha = sc.nextInt();
            sc.nextLine(); // limpar buffer

            // GERENTE
            if (loggedUser.getTipo().equals("gerente")) {
                switch (escolha) {
                    case 1: // Criar operador
                        System.out.print("Username do novo operador: ");
                        String uname = sc.nextLine();
                        System.out.print("Password: ");
                        String pword = sc.nextLine();
                        userService.addUser(uname, pword, "operador");
                        break;
                    case 2: // Listar utilizadores
                        System.out.println("==== Utilizadores ====");
                        for (Utilizador u : userService.getAllUsers()) {
                            System.out.println(u.getUsername() + " - " + u.getTipo());
                        }
                        break;
                    case 3: // Registrar entrada
                        registrarEntrada(sc, gestao);
                        break;
                    case 4: // Registrar saída
                        registrarSaida(sc, gestao);
                        break;
                    case 5: // Listar carros
                        gestao.listarVagas();
                        break;
                    case 0:
                        auth.logout();
                        System.out.println("Sessão terminada.");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            } else { // OPERADOR
                switch (escolha) {
                    case 1:
                        registrarEntrada(sc, gestao);
                        break;
                    case 2:
                        registrarSaida(sc, gestao);
                        break;
                    case 3:
                        gestao.listarVagas();
                        break;
                    case 0:
                        auth.logout();
                        System.out.println("Sessão terminada.");
                        return;
                    default:
                        System.out.println("Opção inválida!");
                }
            }
        }
    }

    // Função para registrar entrada do carro
    private static void registrarEntrada(Scanner sc, GestaoEstacionamento gestao) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        System.out.print("Marca: ");
        String marca = sc.nextLine();
        System.out.print("Modelo: ");
        String modelo = sc.nextLine();
        System.out.print("Cor: ");
        String cor = sc.nextLine();
        System.out.print("Ano: ");
        int ano = sc.nextInt();
        sc.nextLine();
        gestao.registrarEntrada(placa, marca, modelo, cor, ano);
    }

    // Função para registrar saída do carro
    private static void registrarSaida(Scanner sc, GestaoEstacionamento gestao) {
        System.out.print("Placa: ");
        String placa = sc.nextLine();
        gestao.registrarSaida(placa);
    }
}
