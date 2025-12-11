package com.example.app.menu.login;

import com.example.model.Utilizador;
import com.example.service.AuthService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuLogin {

    // Logger para auditoria, segurança e depuração
    private static final Logger logger = LogManager.getLogger(MenuLogin.class);

    // Serviço responsável por autenticação
    private final AuthService auth;

    // I/O configuráveis para permitir testes automatizados
    private final Scanner sc;
    private final PrintStream out;

    // Códigos ANSI para estilização do terminal
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    // Método auxiliar usado exclusivamente para testes unitários
    // Permite validar a lógica de autenticação sem depender de I/O
    public Utilizador login(String username, String password) {
        return auth.login(username, password);
    }

    // Construtor principal usado em testes
    public MenuLogin(AuthService auth, Scanner sc, PrintStream out) {
        this.auth = auth;
        this.sc = sc;
        this.out = out;
    }

    // Construtor padrão para execução real
    public MenuLogin(AuthService auth) {
        this(auth, new Scanner(System.in), System.out);
    }

    // Exibe o cabeçalho visual do sistema de login
    private void mostrarHeader() {
        out.println(CYAN + BOLD + "╔══════════════════════════════════════╗");
        out.println("║              LOGIN SYSTEM            ║");
        out.println("╚══════════════════════════════════════╝" + RESET);
    }

    // Loop principal do menu que conduz o processo de login
    public Utilizador mostrar() {
        while (true) {

            mostrarHeader();  // Desenha o header do menu

            // Solicita credenciais ao utilizador
            out.print(YELLOW + "→ Username: " + RESET);
            String username = sc.nextLine().trim();

            out.print(YELLOW + "→ Password: " + RESET);
            String password = sc.nextLine();

            // Tenta autenticar o utilizador
            Utilizador user = auth.login(username, password);

            // Caso o login seja bem sucedido
            if (user != null) {
                out.println("\n" + GREEN + BOLD + "✔ Login efetuado com sucesso!" + RESET);
                logger.info("Login efetuado: {}", username);
                return user;  // Retorna o utilizador autenticado
            }

            // Caso as credenciais sejam inválidas
            out.println("\n" + RED + BOLD + "✖ Credenciais inválidas! Tente novamente.\n" + RESET);
            logger.warn("Tentativa falhada de login: {}", username);

            // Linha separadora para clareza visual
            out.println(CYAN + "----------------------------------------" + RESET);
        }
    }
}
