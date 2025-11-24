package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.PaymentService;
import com.example.service.UserService;

import java.util.Scanner;

public class MenuPagamentos {

    private final UserService userService;
    private final PaymentService paymentService = new PaymentService();
    private final Scanner sc = new Scanner(System.in);

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuPagamentos(UserService userService) {
        this.userService = userService;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════╗");
        System.out.println("║                   PAGAMENTOS                  ║");
        System.out.println("╚═══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {
        header();

        System.out.print(YELLOW + "→ Ano: " + RESET);
        int ano = Integer.parseInt(sc.nextLine());

        System.out.print(YELLOW + "→ Mês (1-12): " + RESET);
        int mes = Integer.parseInt(sc.nextLine());

        System.out.print(YELLOW + "→ Username do operador: " + RESET);
        String user = sc.nextLine();

        Utilizador u = userService.getByUsername(user);
        if (u == null) {
            System.out.println(RED + "❌ Utilizador não encontrado." + RESET);
            return;
        }

        System.out.println(GREEN + "\nCalculando pagamento..." + RESET);
        paymentService.calcularPagamentoMensal(u, ano, mes);

        System.out.println(YELLOW + "\nPressione Enter para voltar..." + RESET);
        sc.nextLine(); // Espera que o utilizador pressione Enter
    }
}
