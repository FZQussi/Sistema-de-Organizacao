package com.example.app.menu;

import com.example.model.Utilizador;
import com.example.service.UserService;

import java.util.Scanner;

public class MenuGestaoUtilizadores {

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    public MenuGestaoUtilizadores(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {

        int opcao;

        do {
            System.out.println("\n==== Gestão de Utilizadores ====");
            System.out.println("1 - Listar utilizadores");
            System.out.println("2 - Criar utilizador");
            System.out.println("3 - Alterar utilizador");
            System.out.println("4 - Remover utilizador");
            System.out.println("0 - Voltar ao menu anterior");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> listar();
                case 2 -> criar();
                case 3 -> alterar();
                case 4 -> remover();
                case 0 -> System.out.println("A voltar…");
                default -> System.out.println("Opção inválida!");
            }

        } while (opcao != 0);
    }

    private void listar() {
        System.out.println("\n==== Utilizadores ====");
        for (Utilizador u : userService.getAllUsers()) {
            System.out.println(u.getUsername() + " - " + u.getTipo());
        }
    }

    private void criar() {
        System.out.print("Novo username: ");
        String uname = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        userService.addUser(uname, pass, "operador");
    }

    private void alterar() {
        System.out.print("Username a alterar: ");
        String uname = sc.nextLine();

        System.out.print("Nova password: ");
        String pass = sc.nextLine();

        System.out.print("Novo tipo (operador/gerente): ");
        String tipo = sc.nextLine();

        userService.updateUser(uname, pass, tipo);
    }

    private void remover() {
        System.out.print("Username a remover: ");
        String uname = sc.nextLine();

        userService.removeUser(uname);
    }
}
