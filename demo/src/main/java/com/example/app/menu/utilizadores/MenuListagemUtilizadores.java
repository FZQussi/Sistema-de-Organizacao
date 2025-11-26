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

    // ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuListagemUtilizadores(UserService userService) {
        this.userService = userService;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║             LISTAGEM DE UTILIZADORES          ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {
        int opcao;

        do {
            ConsoleUtils.clear();
            header();

            System.out.println(YELLOW + "1" + RESET + " - Listar todos (ordenado)");
            System.out.println(YELLOW + "2" + RESET + " - Filtrar por tipo");
            System.out.println(YELLOW + "3" + RESET + " - Procurar por nome");
            System.out.println(YELLOW + "4" + RESET + " - Listar com paginacão");
            System.out.println(YELLOW + "0" + RESET + " - Voltar\n");

            System.out.print(CYAN + "→ Escolha: " + RESET);

            try {
                opcao = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println(RED + "\n✖ Opcão inválida! Digite um número." + RESET);
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
                        System.out.println(RED + "\n✖ Opcão inválida. Tente novamente.\n" + RESET);
                        logger.warn("Opcão inválida: {}", opcao);
                    }
                }
            }

            if (opcao != 0) {
                System.out.println(YELLOW + "\nPrima ENTER para continuar..." + RESET);
                sc.nextLine();
            }

        } while (opcao != 0);
    }

    private void listarOrdenado() {
        mostrarLista(userService.listarOrdenado());
    }

    private void filtrarTipo() {
        System.out.print(CYAN + "→ Tipo (operador/gerente): " + RESET);
        String tipo = sc.nextLine().trim();
        mostrarLista(userService.listarPorTipo(tipo));
    }

    private void procurarNome() {
        System.out.print(CYAN + "→ Nome: " + RESET);
        mostrarLista(userService.buscarPorNome(sc.nextLine().trim()));
    }

    private void listarPaginado() {
        try {
            System.out.print(CYAN + "→ Página: " + RESET);
            int pg = Integer.parseInt(sc.nextLine().trim());

            System.out.print(CYAN + "→ Tamanho: " + RESET);
            int tam = Integer.parseInt(sc.nextLine().trim());

            mostrarLista(userService.listarPaginado(pg, tam));

        } catch (NumberFormatException e) {
            System.out.println(RED + "\n✖ Entrada inválida! Digite números válidos." + RESET);
            logger.warn("Entrada inválida na listagem paginada.", e);
        }
    }

    private void mostrarLista(List<Utilizador> lista) {

        if (lista.isEmpty()) {
            System.out.println(RED + "\nNenhum utilizador encontrado." + RESET);
            return;
        }

        System.out.println(CYAN + BOLD + "\n╔════╦═════════════════╦═════════════════════════════╦══════════╗");
        System.out.println("║ Nº ║   USERNAME      ║          NOME COMPLETO      ║   TIPO   ║");
        System.out.println("╠════╬═════════════════╬═════════════════════════════╬══════════╣" + RESET);

        int i = 1;
        for (Utilizador u : lista) {
            System.out.printf(
                    "║ %-2d ║ %-15s ║ %-27s ║ %-8s ║\n",
                    i++,
                    u.getUsername(),
                    (u.getNome() + " " + u.getSobrenome()),
                    u.getTipo()
            );
        }

        System.out.println(CYAN + "╚════╩═════════════════╩═════════════════════════════╩══════════╝" + RESET);
    }
}

