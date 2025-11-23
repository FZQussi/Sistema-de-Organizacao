package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.PaymentService;
import com.example.service.UserService;

import java.util.Scanner;

public class MenuPagamentos {

    private final UserService userService;
    private final PaymentService paymentService = new PaymentService();
    private final Scanner sc = new Scanner(System.in);

    public MenuPagamentos(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {
        System.out.println("\n=== Pagamentos ===");

        System.out.print("Ano: ");
        int ano = Integer.parseInt(sc.nextLine());

        System.out.print("Mês (1-12): ");
        int mes = Integer.parseInt(sc.nextLine());

        System.out.print("Username do operador: ");
        String user = sc.nextLine();

        Utilizador u = userService.getByUsername(user);
        if (u == null) {
            System.out.println("❌ Utilizador não encontrado.");
            return;
        }

        paymentService.calcularPagamentoMensal(u, ano, mes);
    }
}
