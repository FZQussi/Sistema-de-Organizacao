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

    public MenuCriarUtilizador(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {
        ConsoleUtils.clear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String dataNascimento;

        System.out.println("\n=== Criar Novo Utilizador ===");
        System.out.println("Digite 'ESC' a qualquer momento para cancelar e voltar ao menu anterior.\n");

        System.out.print("Novo username: ");
        String username = sc.nextLine();
        if (username.equalsIgnoreCase("ESC")) { cancel(); return; }

        System.out.print("Password: ");
        String pass = sc.nextLine();
        if (pass.equalsIgnoreCase("ESC")) { cancel(); return; }

        String hash = PasswordUtils.hash(pass);
        logger.debug("Password digitada: {}, Hash gerado: {}", pass, hash);

        System.out.print("Nome: ");
        String nome = sc.nextLine();
        if (nome.equalsIgnoreCase("ESC")) { cancel(); return; }

        System.out.print("Sobrenome: ");
        String sobrenome = sc.nextLine();
        if (sobrenome.equalsIgnoreCase("ESC")) { cancel(); return; }

        System.out.print("Descrição: ");
        String descricao = sc.nextLine();
        if (descricao.equalsIgnoreCase("ESC")) { cancel(); return; }

        // --- Nacionalidade com validação ---
        List<String> nacionalidadesValidas = FileUtils.carregarNacionalidades();
        String nacionalidade;
        while (true) {
            System.out.print("Nacionalidade: ");
            nacionalidade = sc.nextLine();
            if (nacionalidade.equalsIgnoreCase("ESC")) { cancel(); return; }

            if (nacionalidadesValidas.contains(nacionalidade)) {
                break;
            } else {
                System.out.println("\n❌ Nacionalidade inválida!");
                System.out.println("Valores aceites: " + nacionalidadesValidas);
                logger.warn("Nacionalidade inválida digitada: {}", nacionalidade);
            }
        }

        // --- Data de nascimento com validação ---
        while (true) {
            System.out.print("Data de nascimento (AAAA-MM-DD): ");
            dataNascimento = sc.nextLine();
            if (dataNascimento.equalsIgnoreCase("ESC")) { cancel(); return; }

            try {
                LocalDate.parse(dataNascimento, formatter);
                break;
            } catch (DateTimeParseException e) {
                System.out.println("❌ Formato inválido. Use: AAAA-MM-DD");
                logger.warn("Data de nascimento inválida digitada: {}", dataNascimento);
            }
        }

        System.out.print("Salário: ");
        String salInput = sc.nextLine();
        if (salInput.equalsIgnoreCase("ESC")) { cancel(); return; }
        double salario = Double.parseDouble(salInput);

        System.out.print("Turno entrada (HH:mm): ");
        String horaEntrada = sc.nextLine();
        if (horaEntrada.equalsIgnoreCase("ESC")) { cancel(); return; }

        System.out.print("Turno saída (HH:mm): ");
        String horaSaida = sc.nextLine();
        if (horaSaida.equalsIgnoreCase("ESC")) { cancel(); return; }

        String tipo;
        do {
            System.out.print("Tipo (operador/gerente): ");
            tipo = sc.nextLine();
            if (tipo.equalsIgnoreCase("ESC")) { cancel(); return; }
            tipo = tipo.toLowerCase();
        } while (!tipo.equals("operador") && !tipo.equals("gerente"));

        Utilizador novo = new Utilizador(
            username, hash, tipo, nome, sobrenome,
            descricao, nacionalidade, dataNascimento,
            salario, horaEntrada, horaSaida
        );

        userService.addUser(novo);
        logger.info("Novo utilizador criado: {} ({})", username, tipo);

        System.out.println("\n✔ Utilizador criado com sucesso!");
    }

    private void cancel() {
        System.out.println("\n⚠ Operação cancelada. Voltando ao menu anterior.");
        logger.info("Operação de criação de utilizador cancelada pelo utilizador.");
    }
}
