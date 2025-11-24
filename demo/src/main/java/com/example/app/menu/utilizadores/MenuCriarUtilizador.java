package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.ConsoleUtils;
import com.example.utils.FileUtils;
import com.example.utils.PasswordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuCriarUtilizador {

    private static final Logger logger = LogManager.getLogger(MenuCriarUtilizador.class);

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    // --- Cores ANSI ---
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuCriarUtilizador(UserService userService) {
        this.userService = userService;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════════╗");
        System.out.println("║            CRIAR NOVO UTILIZADOR             ║");
        System.out.println("╚══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {
        ConsoleUtils.clear();
        header();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println(YELLOW + "Digite 'ESC' para cancelar a qualquer momento.\n" + RESET);

        // --- Username ---
        System.out.print(YELLOW + "→ Novo username: " + RESET);
        String username = sc.nextLine();
        if (username.equalsIgnoreCase("ESC")) { cancel(); return; }

        // --- Password ---
        System.out.print(YELLOW + "→ Password: " + RESET);
        String pass = sc.nextLine();
        if (pass.equalsIgnoreCase("ESC")) { cancel(); return; }

        String hash = PasswordUtils.hash(pass);
        logger.debug("Password digitada: {}, Hash gerado: {}", pass, hash);

        // --- Nome ---
        System.out.print(YELLOW + "→ Nome: " + RESET);
        String nome = sc.nextLine();
        if (nome.equalsIgnoreCase("ESC")) { cancel(); return; }

        // --- Sobrenome ---
        System.out.print(YELLOW + "→ Sobrenome: " + RESET);
        String sobrenome = sc.nextLine();
        if (sobrenome.equalsIgnoreCase("ESC")) { cancel(); return; }

        // --- Descrição ---
        System.out.print(YELLOW + "→ Descrição: " + RESET);
        String descricao = sc.nextLine();
        if (descricao.equalsIgnoreCase("ESC")) { cancel(); return; }

        // --- Nacionalidade validada ---
        List<String> nacionalidadesValidas = FileUtils.carregarNacionalidades();
        String nacionalidade;

        while (true) {
            System.out.print(YELLOW + "→ Nacionalidade: " + RESET);
            nacionalidade = sc.nextLine();

            if (nacionalidade.equalsIgnoreCase("ESC")) { cancel(); return; }

            if (nacionalidadesValidas.contains(nacionalidade)) {
                break;
            }

            System.out.println(RED + "✖ Nacionalidade inválida!" + RESET);
            System.out.println(YELLOW + "Valores aceites: " + nacionalidadesValidas + RESET);
            logger.warn("Nacionalidade inválida digitada: {}", nacionalidade);
        }

        // --- Data de nascimento ---
        String dataNascimento;
        while (true) {
            System.out.print(YELLOW + "→ Data de nascimento (AAAA-MM-DD): " + RESET);
            dataNascimento = sc.nextLine();

            if (dataNascimento.equalsIgnoreCase("ESC")) { cancel(); return; }

            try {
                LocalDate.parse(dataNascimento, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println(RED + "✖ Formato inválido! Use AAAA-MM-DD." + RESET);
                logger.warn("Data inválida digitada: {}", dataNascimento);
            }
        }

        // --- Salário ---
        System.out.print(YELLOW + "→ Salário: " + RESET);
        String salInput = sc.nextLine();
        if (salInput.equalsIgnoreCase("ESC")) { cancel(); return; }
        double salario = Double.parseDouble(salInput);

        // --- Turno entrada ---
        System.out.print(YELLOW + "→ Turno entrada (HH:mm): " + RESET);
        String horaEntrada = sc.nextLine();
        if (horaEntrada.equalsIgnoreCase("ESC")) { cancel(); return; }

        // --- Turno saída ---
        System.out.print(YELLOW + "→ Turno saída (HH:mm): " + RESET);
        String horaSaida = sc.nextLine();
        if (horaSaida.equalsIgnoreCase("ESC")) { cancel(); return; }

        // --- Tipo ---
        String tipo;
        do {
            System.out.print(YELLOW + "→ Tipo (operador/gerente): " + RESET);
            tipo = sc.nextLine();

            if (tipo.equalsIgnoreCase("ESC")) { cancel(); return; }

            tipo = tipo.toLowerCase();
        } while (!tipo.equals("operador") && !tipo.equals("gerente"));

        // --- Criar objeto ---
        Utilizador novo = new Utilizador(
                username, hash, tipo, nome, sobrenome,
                descricao, nacionalidade, dataNascimento,
                salario, horaEntrada, horaSaida
        );

        userService.addUser(novo);
        logger.info("Novo utilizador criado: {} ({})", username, tipo);

        System.out.println(CYAN + "\n──────────────────────────────────────────────" + RESET);
        System.out.println(GREEN + BOLD + "✔ Utilizador criado com sucesso!" + RESET);
    }

    private void cancel() {
        System.out.println(RED + "\n⚠ Operação cancelada. Voltando ao menu anterior." + RESET);
        logger.info("Operação de criação cancelada pelo utilizador.");
    }
}
