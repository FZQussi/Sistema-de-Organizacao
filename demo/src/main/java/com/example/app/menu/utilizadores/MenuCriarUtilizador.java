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

    private final UserService userService;   // Serviço responsável pela gestão de utilizadores
    private final Scanner sc = new Scanner(System.in); // Leitor de input do utilizador

    // Códigos ANSI para formatação visual
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuCriarUtilizador(UserService userService) {
        this.userService = userService; // Injeção do serviço de utilizadores
    }

    // Desenha o cabeçalho do menu
    private void header() {
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════════╗");
        System.out.println("║            CRIAR NOVO UTILIZADOR             ║");
        System.out.println("╚══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {
        ConsoleUtils.clear(); // Limpa o ecrã
        header();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println(YELLOW + "Digite 'ESC' para cancelar a qualquer momento.\n" + RESET);

        // Captura do username
        System.out.print(YELLOW + "→ Novo username: " + RESET);
        String username = sc.nextLine();
        if (username.equalsIgnoreCase("ESC")) { cancel(); return; }

        // Captura da password e hashing
        System.out.print(YELLOW + "→ Password: " + RESET);
        String pass = sc.nextLine();
        if (pass.equalsIgnoreCase("ESC")) { cancel(); return; }

        String hash = PasswordUtils.hash(pass); // Gera hash seguro
        logger.debug("Password digitada: {}, Hash gerado: {}", pass, hash);

        // Captura do nome
        System.out.print(YELLOW + "→ Nome: " + RESET);
        String nome = sc.nextLine();
        if (nome.equalsIgnoreCase("ESC")) { cancel(); return; }

        // Captura do sobrenome
        System.out.print(YELLOW + "→ Sobrenome: " + RESET);
        String sobrenome = sc.nextLine();
        if (sobrenome.equalsIgnoreCase("ESC")) { cancel(); return; }

        // Captura descrição
        System.out.print(YELLOW + "→ Descricão: " + RESET);
        String descricao = sc.nextLine();
        if (descricao.equalsIgnoreCase("ESC")) { cancel(); return; }

        // Validação de nacionalidade
        List<String> nacionalidadesValidas = FileUtils.carregarNacionalidades();
        String nacionalidade;

        while (true) {
            System.out.print(YELLOW + "→ Nacionalidade: " + RESET);
            nacionalidade = sc.nextLine();

            if (nacionalidade.equalsIgnoreCase("ESC")) { cancel(); return; }

            if (nacionalidadesValidas.contains(nacionalidade)) {
                break; // Nacionalidade válida
            }

            System.out.println(RED + "✖ Nacionalidade inválida!" + RESET);
            System.out.println(YELLOW + "Valores aceites: " + nacionalidadesValidas + RESET);
            logger.warn("Nacionalidade inválida digitada: {}", nacionalidade);
        }

        // Validação de data de nascimento
        String dataNascimento;
        while (true) {
            System.out.print(YELLOW + "→ Data de nascimento (AAAA-MM-DD): " + RESET);
            dataNascimento = sc.nextLine();

            if (dataNascimento.equalsIgnoreCase("ESC")) { cancel(); return; }

            try {
                LocalDate.parse(dataNascimento, formatter);
                break; // Data válida
            } catch (DateTimeParseException e) {
                System.out.println(RED + "✖ Formato inválido! Use AAAA-MM-DD." + RESET);
                logger.warn("Data inválida digitada: {}", dataNascimento);
            }
        }

        // Salário
        System.out.print(YELLOW + "→ Salário: " + RESET);
        String salInput = sc.nextLine();
        if (salInput.equalsIgnoreCase("ESC")) { cancel(); return; }
        double salario = Double.parseDouble(salInput); // Conversão para double

        // Turno entrada
        System.out.print(YELLOW + "→ Turno entrada (HH:mm): " + RESET);
        String horaEntrada = sc.nextLine();
        if (horaEntrada.equalsIgnoreCase("ESC")) { cancel(); return; }

        // Turno saída
        System.out.print(YELLOW + "→ Turno saída (HH:mm): " + RESET);
        String horaSaida = sc.nextLine();
        if (horaSaida.equalsIgnoreCase("ESC")) { cancel(); return; }

        // Tipo de utilizador
        String tipo;
        do {
            System.out.print(YELLOW + "→ Tipo (operador/gerente): " + RESET);
            tipo = sc.nextLine();

            if (tipo.equalsIgnoreCase("ESC")) { cancel(); return; }

            tipo = tipo.toLowerCase();
        } while (!tipo.equals("operador") && !tipo.equals("gerente")); // Garantir valor válido

        // Construção do objeto Utilizador
        Utilizador novo = new Utilizador(
                username, hash, tipo, nome, sobrenome,
                descricao, nacionalidade, dataNascimento,
                salario, horaEntrada, horaSaida
        );

        userService.addUser(novo); // Persistência do utilizador
        logger.info("Novo utilizador criado: {} ({})", username, tipo);

        System.out.println(CYAN + "\n──────────────────────────────────────────────" + RESET);
        System.out.println(GREEN + BOLD + "✔ Utilizador criado com sucesso!" + RESET);
    }

    // Cancelamento da operação
    private void cancel() {
        System.out.println(RED + "\n⚠ Operacão cancelada. Voltando ao menu anterior." + RESET);
        logger.info("Opercão de criacão cancelada pelo utilizador.");
    }

    // Método auxiliar para testes automatizados
    public Utilizador criarUtilizadorTest(
            String username,
            String password,
            String nome,
            String sobrenome,
            String descricao,
            String nacionalidade,
            String dataNascimento,
            double salario,
            String turnoEntrada,
            String turnoSaida,
            String tipo
    ) {
        // Validação simplificada de nacionalidade
        List<String> nacionaisValidas = FileUtils.carregarNacionalidades();
        if (!nacionaisValidas.contains(nacionalidade)) {
            return null;
        }

        // Validação simples de data
        try {
            LocalDate.parse(dataNascimento);
        } catch (Exception e) {
            return null;
        }

        // Hash seguro da password
        String hash = PasswordUtils.hash(password);

        Utilizador novo = new Utilizador(
                username, hash, tipo, nome, sobrenome, descricao,
                nacionalidade, dataNascimento, salario,
                turnoEntrada, turnoSaida
        );

        userService.addUser(novo); // Persistência
        return novo;
    }

}
