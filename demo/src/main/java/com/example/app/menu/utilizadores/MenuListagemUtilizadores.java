package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.ConsoleUtils;
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
            // Limpar consola a cada abertura do menu
            ConsoleUtils.clear();

            System.out.println("\n==== Listagem de Utilizadores ====");
            System.out.println("1 - Listar todos (ordenado)");
            System.out.println("2 - Filtrar por tipo");
            System.out.println("3 - Procurar por nome");
            System.out.println("4 - Listar com paginação");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Opção inválida! Digite um número.");
                logger.warn("Entrada inválida no menu de listagem de utilizadores.", e);
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> listarOrdenado();
                case 2 -> filtrarTipo();
                case 3 -> procurarNome();
                case 4 -> listarPaginado();
                case 0 -> logger.info("Voltando ao menu anterior.");
                default -> {
                    if (opcao != 0) {
                        System.out.println("❌ Opção inválida. Tente novamente.");
                        logger.warn("Opção inválida: {}", opcao);
                    }
                }
            }

            System.out.println("\nPressione ENTER para continuar...");
            sc.nextLine(); // Pausa antes de limpar e voltar ao menu

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
        try {
            System.out.print("Página: ");
            int pg = Integer.parseInt(sc.nextLine());

            System.out.print("Tamanho: ");
            int tam = Integer.parseInt(sc.nextLine());

            mostrarLista(userService.listarPaginado(pg, tam));
        } catch (NumberFormatException e) {
            System.out.println("❌ Entrada inválida! Digite números válidos.");
            logger.warn("Entrada inválida na listagem paginada.", e);
        }
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
