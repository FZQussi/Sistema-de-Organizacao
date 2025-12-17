package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

/**
 * Menu responsável pela remoção de utilizadores do sistema.
 * Inclui confirmação explícita antes de efetuar a remoção.
 */
public class MenuRemoverUtilizador {

    // Logger para registo de ações e tentativas inválidas
    private static final Logger logger = LogManager.getLogger(MenuRemoverUtilizador.class);

    // Serviço de utilizadores que contém a lógica de remoção
    private final UserService userService;

    // Scanner para leitura de entradas do utilizador
    private final Scanner sc = new Scanner(System.in);

    // Códigos ANSI para cores e formatação do terminal
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    /**
     * Construtor do menu de remoção de utilizadores.
     *
     * @param userService serviço responsável pela gestão de utilizadores
     */
    public MenuRemoverUtilizador(UserService userService) {
        this.userService = userService;
    }

    /**
     * Imprime o cabeçalho do menu de remoção.
     */
    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║               REMOVER UTILIZADOR             ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    /**
     * Método principal do menu.
     * Solicita o username, valida a existência do utilizador
     * e pede confirmação antes de efetuar a remoção.
     */
    public void mostrar() {

        // Limpa a consola e mostra o cabeçalho
        ConsoleUtils.clear();
        header();

        // Informação ao utilizador sobre a possibilidade de cancelamento
        System.out.println(YELLOW + "Digite '0' para cancelar e voltar ao menu anterior.\n" + RESET);

        // Leitura do username a remover
        System.out.print(YELLOW + "→ Username do utilizador a remover: " + RESET);
        String username = sc.nextLine();

        // Cancelamento explícito da operação
        if (username.equals("0")) {
            System.out.println(RED + "❌ Ação cancelada. Voltando ao menu anterior." + RESET);
            return;
        }

        // Pesquisa do utilizador pelo username
        Utilizador u = userService.getByUsername(username);

        // Validação da existência do utilizador
        if (u == null) {
            System.out.println(RED + "❌ Utilizador não encontrado." + RESET);
            logger.warn("Tentativa de remover utilizador inexistente: {}", username);
            return;
        }

        // Apresentação dos dados do utilizador encontrado
        System.out.println(GREEN + "\nEncontrado:" + RESET);
        System.out.println(" - Nome: " + u.getNome() + " " + u.getSobrenome());
        System.out.println(" - Tipo: " + u.getTipo());

        // Pedido de confirmação final
        System.out.print(YELLOW + "\nTem a certeza que deseja remover? (s/n ou 0 para cancelar): " + RESET);
        String confirm = sc.nextLine().toLowerCase();

        // Caso a confirmação não seja afirmativa, cancela a operação
        if (!confirm.equals("s")) {
            System.out.println(RED + "❌ Ação cancelada." + RESET);
            logger.info("Remoção cancelada pelo utilizador para '{}'", username);
            return;
        }

        // Remoção efetiva do utilizador
        userService.removeUser(username);
        System.out.println(GREEN + "✔ Utilizador removido com sucesso!" + RESET);
        logger.info("Utilizador removido: {}", username);
    }
}
