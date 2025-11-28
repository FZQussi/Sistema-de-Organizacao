package com.example.app.menu.utilizadores;

import com.example.service.UserService;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuGestaoUtilizadores {

    private static final Logger logger = LogManager.getLogger(MenuGestaoUtilizadores.class);

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuGestaoUtilizadores(UserService userService) {
        this.userService = userService;
    }

    // ============================================================
    // MÉTODOS FACTORY — permitem substituir menus durante testes
    // ============================================================

    protected MenuListagemUtilizadores criarMenuListagem() {
        return new MenuListagemUtilizadores(userService);
    }

    protected MenuCriarUtilizador criarMenuCriar() {
        return new MenuCriarUtilizador(userService);
    }

    protected MenuAlterarUtilizador criarMenuAlterar() {
        return new MenuAlterarUtilizador(userService);
    }

    protected MenuRemoverUtilizador criarMenuRemover() {
        return new MenuRemoverUtilizador(userService);
    }

    protected MenuPagamentos criarMenuPagamentos() {
        return new MenuPagamentos(userService);
    }

    // ============================================================

    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║             GESTÃO DE UTILIZADORES            ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {
        int opcao;

        do {
            ConsoleUtils.clear();
            header();

            System.out.println(YELLOW + "1" + RESET + " - Listar / Procurar utilizadores");
            System.out.println(YELLOW + "2" + RESET + " - Criar utilizador");
            System.out.println(YELLOW + "3" + RESET + " - Alterar utilizador");
            System.out.println(YELLOW + "4" + RESET + " - Remover utilizador");
            System.out.println(YELLOW + "5" + RESET + " - Pagamentos");
            System.out.println(YELLOW + "0" + RESET + " - Voltar\n");

            System.out.print(CYAN + "→ Escolha: " + RESET);

            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(RED + "\n✖ Opcão inválida! Digite um número.\n" + RESET);
                logger.warn("Entrada inválida no menu de gestão de utilizadores.", e);
                opcao = -1;
                continue;
            }

            switch (opcao) {
                case 1 -> criarMenuListagem().mostrar();
                case 2 -> criarMenuCriar().mostrar();
                case 3 -> criarMenuAlterar().mostrar();
                case 4 -> criarMenuRemover().mostrar();
                case 5 -> criarMenuPagamentos().mostrar();

                case 0 -> {
                    logger.info("Voltando ao menu anterior.");
                    System.out.println(GREEN + "\n✔ A voltar ao menu principal..." + RESET);
                }

                default -> {
                    System.out.println(RED + "\n✖ Opcão inválida. Tente novamente.\n" + RESET);
                    logger.warn("Opção inválida: {}", opcao);
                }
            }

        } while (opcao != 0);
    }
}
