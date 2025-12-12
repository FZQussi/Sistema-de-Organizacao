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

    // Paleta de cores ANSI usada exclusivamente para formatação visual no terminal
    private static final String RESET = "[0m";
    private static final String CYAN = "[36m";
    private static final String GREEN = "[32m";
    private static final String RED = "[31m";
    private static final String YELLOW = "[33m";
    private static final String BOLD = "[1m";

    // Injeção do serviço principal responsável pelo CRUD de utilizadores
    public MenuGestaoUtilizadores(UserService userService) {
        this.userService = userService;
    }

    // Métodos factory permitem substituir menus em testes unitários, melhorando a testabilidade e isolamento
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

    // Renderização do cabeçalho principal do submenu de gestão
    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║             GESTÃO DE UTILIZADORES            ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    // Loop principal do menu, controla a navegação entre submenus e valida escolhas do utilizador
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
            System.out.println(YELLOW + "0" + RESET + " - Voltar");

            System.out.print(CYAN + "→ Escolha: " + RESET);

            // Tratamento robusto de entrada numérica para evitar que o menu falhe com caracteres inválidos
            try {
                opcao = Integer.parseInt(sc.nextLine());
            } catch (NumberFormatException e) {
                System.out.println(RED + "✖ Opcão inválida! Digite um número." + RESET);
                logger.warn("Entrada inválida no menu de gestão de utilizadores.", e);
                opcao = -1;
                continue;
            }

            // Encaminhamento da escolha para o submenu correspondente
            switch (opcao) {
                case 1 -> criarMenuListagem().mostrar();
                case 2 -> criarMenuCriar().mostrar();
                case 3 -> criarMenuAlterar().mostrar();
                case 4 -> criarMenuRemover().mostrar();
                case 5 -> criarMenuPagamentos().mostrar();

                case 0 -> {
                    logger.info("A voltar ao menu anterior.");
                    System.out.println(GREEN + "✔ A voltar ao menu principal..." + RESET);
                }

                // Qualquer número fora das opções previstas gera aviso e repete o menu
                default -> {
                    System.out.println(RED + "✖ Opcão inválida. Tente novamente." + RESET);
                    logger.warn("Opção inválida selecionada: {}", opcao);
                }
            }

        } while (opcao != 0); // Ciclo mantém-se ativo até o utilizador escolher sair
    }
}
