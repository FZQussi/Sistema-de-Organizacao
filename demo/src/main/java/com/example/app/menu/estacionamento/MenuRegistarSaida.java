package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.Scanner;
import java.util.regex.Pattern;

public class MenuRegistarSaida {

    // Logger para auditoria e depuração
    private static final Logger logger = LogManager.getLogger(MenuRegistarSaida.class);

    // Padrões válidos de matrícula portuguesa
    private static final Pattern MATRICULA_PT = Pattern.compile(
            "^[A-Z]{2}-\\d{2}-\\d{2}$"
                    + "|^\\d{2}-\\d{2}-[A-Z]{2}$"
                    + "|^\\d{2}-[A-Z]{2}-\\d{2}$"
                    + "|^[A-Z]{2}-\\d{2}-[A-Z]{2}$"
    );

    // Dependências principais
    private final GestaoEstacionamento gestao;   // Serviço responsável pela lógica de saída do veículo
    private final Scanner sc;                    // Entrada de dados do utilizador
    private final PrintStream out;               // Saída configurável (console ou mock)

    // Cores ANSI para estilização da interface
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    // Construtor destinado a testes
    public MenuRegistarSaida(GestaoEstacionamento gestao, Scanner sc, PrintStream out) {
        this.gestao = gestao;
        this.sc = sc;
        this.out = out;
    }

    // Construtor padrão
    public MenuRegistarSaida(GestaoEstacionamento gestao) {
        this(gestao, new Scanner(System.in), System.out);
    }

    // Cabeçalho do menu
    private void header() {
        out.println(CYAN + BOLD + "╔══════════════════════════════════════════════╗");
        out.println("║            REGISTAR SAÍDA DE CARRO           ║");
        out.println("╚══════════════════════════════════════════════╝" + RESET);
        out.println();
    }

    // Método protegido para permitir testes unitários isolados
    protected String pedirMatricula() {
        while (true) {
            out.print(YELLOW + "→ Placa (0 para voltar): " + RESET);
            String placa = sc.nextLine().trim().toUpperCase();

            // Retorna ao menu anterior
            if (placa.equals("0")) return "0";

            // Validação com regex de matrículas portuguesas
            if (!MATRICULA_PT.matcher(placa).matches()) {
                out.println(RED + BOLD + "✖ Matrícula inválida!\n" + RESET);
                continue;
            }

            return placa;
        }
    }

    // Fluxo principal do menu
    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();   // Limpa a interface
            header();              // Mostra o cabeçalho

            String placa = pedirMatricula();
            if (placa.equals("0")) return;    // Voltar

            // Tenta registar a saída
            boolean sucesso = gestao.registrarSaida(placa);

            out.println(CYAN + "──────────────────────────────────────────────" + RESET);

            if (sucesso) {
                out.println(GREEN + BOLD + "✔ Saída registada com sucesso!" + RESET);
                logger.info("Saída registada para a placa: {}", placa);
            } else {
                out.println(RED + BOLD + "✖ Falha: Placa não encontrada!" + RESET);
                logger.warn("Falha ao registar saída para a placa: {}", placa);
            }

            out.println(CYAN + "──────────────────────────────────────────────\n" + RESET);

            // Opção de continuar ou voltar
            out.println(YELLOW + "1 - Registar outra saída" + RESET);
            out.println(YELLOW + "0 - Voltar" + RESET);
            out.print(YELLOW + "→ Escolha: " + RESET);

            if (sc.nextLine().trim().equals("0")) return;
        }
    }
}
