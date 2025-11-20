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

    public MenuGestaoUtilizadores(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {
        int opcao;

        do {
            // Limpar a consola a cada exibição do menu
            ConsoleUtils.clear();

            System.out.println("\n==== Gestão de Utilizadores ====");
            System.out.println("1 - Listar / Procurar utilizadores");
            System.out.println("2 - Criar utilizador");
            System.out.println("3 - Alterar utilizador");
            System.out.println("4 - Remover utilizador");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("❌ Opção inválida! Digite um número.");
                logger.warn("Entrada inválida no menu de gestão de utilizadores.", e);
                opcao = -1;
            }

            switch (opcao) {
                case 1 -> new MenuListagemUtilizadores(userService).mostrar();
                case 2 -> new MenuCriarUtilizador(userService).mostrar();
                case 3 -> new MenuAlterarUtilizador(userService).mostrar();
                case 4 -> new MenuRemoverUtilizador(userService).mostrar();
                case 0 -> logger.info("Voltando ao menu anterior.");
                default -> {
                    if (opcao != 0) {
                        System.out.println("❌ Opção inválida. Tente novamente.");
                        logger.warn("Opção inválida: {}", opcao);
                    }
                }
            }

        } while (opcao != 0);
    }
}

