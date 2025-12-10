package com.example.app.menu.login;

import com.example.model.Utilizador;
import com.example.service.AuthServicetest;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuLogin {

    private static final Logger logger = LogManager.getLogger(MenuLogin.class);
    private final AuthServicetest auth;
    private final Scanner sc;
    private final PrintStream out;

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";
    // Método auxiliar só para testes (sem scanner)
    public Utilizador login(String username, String password) {
        return auth.login(username, password);
    }

    // Construtor testável
    public MenuLogin(AuthServicetest auth, Scanner sc, PrintStream out) {
        this.auth = auth;
        this.sc = sc;
        this.out = out;
    }

    // Construtor normal para uso real
    public MenuLogin(AuthServicetest auth) {
        this(auth, new Scanner(System.in), System.out);
    }

    private void mostrarHeader() {
        out.println(CYAN + BOLD + "╔══════════════════════════════════════╗");
        out.println("║              LOGIN SYSTEM            ║");
        out.println("╚══════════════════════════════════════╝" + RESET);
    }

    public Utilizador mostrar() {
        while (true) {

            mostrarHeader();

            out.print(YELLOW + "→ Username: " + RESET);
            String username = sc.nextLine().trim();

            out.print(YELLOW + "→ Password: " + RESET);
            String password = sc.nextLine();

            Utilizador user = auth.login(username, password);

            if (user != null) {
                out.println("\n" + GREEN + BOLD + "✔ Login efetuado com sucesso!" + RESET);
                logger.info("Login efetuado: {}", username);
                return user;
            }

            out.println("\n" + RED + BOLD + "✖ Credenciais inválidas! Tente novamente.\n" + RESET);
            logger.warn("Tentativa falhada de login: {}", username);

            out.println(CYAN + "----------------------------------------" + RESET);
        }
    }
}
