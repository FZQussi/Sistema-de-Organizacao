package com.example.app.menu.login;

import com.example.model.Utilizador;
import com.example.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Scanner;

public class MenuLogin {

    private static final Logger logger = LogManager.getLogger(MenuLogin.class);
    private final AuthService auth;
    private final Scanner sc = new Scanner(System.in);

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuLogin(AuthService auth) {
        this.auth = auth;
    }

    private void mostrarHeader() {
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════╗");
        System.out.println("║              LOGIN SYSTEM            ║");
        System.out.println("╚══════════════════════════════════════╝" + RESET);
    }

    public Utilizador mostrar() {
        while (true) {

            mostrarHeader();

            System.out.print(YELLOW + "→ Username: " + RESET);
            String username = sc.nextLine().trim();

            System.out.print(YELLOW + "→ Password: " + RESET);
            String password = sc.nextLine();

            Utilizador user = auth.login(username, password);

            if (user != null) {
                System.out.println("\n" + GREEN + BOLD + "✔ Login efetuado com sucesso!" + RESET);
                logger.info("Login efetuado: {}", username);
                return user;
            }

            System.out.println("\n" + RED + BOLD + "✖ Credenciais inválidas! Tente novamente.\n" + RESET);
            logger.warn("Tentativa falhada de login: {}", username);

            System.out.println(CYAN + "----------------------------------------" + RESET);
        }
    }
}

