package com.example.app.menu;

import com.example.model.Utilizador;
import com.example.service.UserService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.util.List;
import java.util.Scanner;

public class MenuListagemUtilizadores {

    private static final Logger logger = LogManager.getLogger(MenuListagemUtilizadores.class);

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    public MenuListagemUtilizadores(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {
        int opcao;

        do {
            System.out.println("\n==== Listagem de Utilizadores ====");
            System.out.println("1 - Listar todos (ordenado)");
            System.out.println("2 - Filtrar por tipo");
            System.out.println("3 - Procurar por nome");
            System.out.println("4 - Listar com paginação");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> listarOrdenado();
                case 2 -> filtrarTipo();
                case 3 -> procurarNome();
                case 4 -> listarPaginado();
                case 0 -> logger.info("Voltando ao menu anterior.");
                default -> logger.warn("Opção inválida: {}", opcao);
            }

        } while (opcao != 0);
    }

    private void listarOrdenado() {
        List<Utilizador> lista = userService.listarOrdenado();
        mostrarLista(lista);
    }

    private void filtrarTipo() {
        System.out.print("Tipo (operador/gerente): ");
        String tipo = sc.nextLine();
        mostrarLista(userService.listarPorTipo(tipo));
    }

    private void procurarNome() {
        System.out.print("Nome: ");
        mostrarLista(userService.buscarPorNome(sc.nextLine()));
    }

    private void listarPaginado() {
        System.out.print("Página: ");
        int pg = Integer.parseInt(sc.nextLine());

        System.out.print("Tamanho: ");
        int tam = Integer.parseInt(sc.nextLine());

        mostrarLista(userService.listarPaginado(pg, tam));
    }

    private void mostrarLista(List<Utilizador> lista) {
        if (lista.isEmpty()) {
            System.out.println("Nenhum utilizador encontrado.");
            return;
        }

        int i = 1;
        for (Utilizador u : lista) {
            System.out.println(i++ + ". " + u.getUsername() +
                    " | " + u.getNome() + " " + u.getSobrenome() +
                    " | " + u.getTipo());
        }
    }
}
