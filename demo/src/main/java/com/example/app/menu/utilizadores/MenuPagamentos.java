package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.PaymentService;
import com.example.service.UserService;

import java.util.Scanner;

/**
 * Menu responsável pelo cálculo de pagamentos mensais de operadores.
 * Permite selecionar o período e o utilizador para efetuar o cálculo.
 */
public class MenuPagamentos {

    // Serviço de utilizadores para validação e obtenção de dados
    private final UserService userService;

    // Serviço responsável pela lógica de cálculo de pagamentos
    private final PaymentService paymentService = new PaymentService();

    // Scanner para leitura de dados introduzidos pelo utilizador
    private final Scanner sc = new Scanner(System.in);

    // Códigos ANSI para cores e formatação no terminal
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    /**
     * Construtor do menu de pagamentos.
     *
     * @param userService serviço de utilizadores utilizado para pesquisa
     */
    public MenuPagamentos(UserService userService) {
        this.userService = userService;
    }

    /**
     * Imprime o cabeçalho do menu de pagamentos.
     */
    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║                   PAGAMENTOS                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    /**
     * Método principal do menu.
     * Solicita os dados necessários e executa o cálculo do pagamento mensal.
     */
    public void mostrar() {
        header();

        // Leitura do ano de referência
        System.out.print(YELLOW + "→ Ano: " + RESET);
        int ano = Integer.parseInt(sc.nextLine());

        // Leitura do mês de referência
        System.out.print(YELLOW + "→ Mês (1-12): " + RESET);
        int mes = Integer.parseInt(sc.nextLine());

        // Leitura do username do operador
        System.out.print(YELLOW + "→ Username do operador: " + RESET);
        String user = sc.nextLine();

        // Pesquisa do utilizador pelo username
        Utilizador u = userService.getByUsername(user);

        // Validação da existência do utilizador
        if (u == null) {
            System.out.println(RED + "❌ Utilizador não encontrado." + RESET);
            return;
        }

        // Início do cálculo do pagamento
        System.out.println(GREEN + "\nCalculando pagamento..." + RESET);
        paymentService.calcularPagamentoMensal(u, ano, mes);

        // Pausa antes de regressar ao menu anterior
        System.out.println(YELLOW + "\nPressione Enter para voltar..." + RESET);
        sc.nextLine(); // Aguarda confirmação do utilizador
    }
}
