package com.example.app.menu.estacionamento;

import com.example.service.GestaoEstacionamento;
import com.example.utils.ConsoleUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.PrintStream;
import java.util.Scanner;

public class MenuListarCarros {

    // Logger para registro de eventos e depuração
    private static final Logger logger = LogManager.getLogger(MenuListarCarros.class);

    // Dependências principais da classe
    private final GestaoEstacionamento gestao;  // Serviço responsável pela lógica do estacionamento
    private final Scanner sc;                   // Scanner para leitura de input do usuário
    private final PrintStream out;              // Saída configurável (System.out ou mock nos testes)

    // Códigos de formatação ANSI para colorir a saída no console
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    // Construtor pensado para testes, permitindo injeção de dependências mockadas
    public MenuListarCarros(GestaoEstacionamento gestao, Scanner sc, PrintStream out) {
        this.gestao = gestao;
        this.sc = sc;
        this.out = out;
    }

    // Construtor padrão utilizado pela aplicação
    public MenuListarCarros(GestaoEstacionamento gestao) {
        this(gestao, new Scanner(System.in), System.out);
    }

    // Exibe o cabeçalho estilizado do menu
    private void header() {
        out.println(CYAN + BOLD + "╔═══════════════════════════════════════════════════╗");
        out.println("║         LISTAGEM DE CARROS NO ESTACIONAMENTO      ║");
        out.println("╚═══════════════════════════════════════════════════╝" + RESET);
        out.println();
    }

    // Método principal que executa o loop da interface de listagem
    public void mostrar() {
        while (true) {
            ConsoleUtils.clear();   // Limpa a tela (mockável nos testes)
            header();               // Exibe o cabeçalho

            out.println(GREEN + BOLD + "Carros atualmente estacionados:\n" + RESET);

            gestao.listarVagas();   // Exibe a listagem de carros; será mockado em testes

            out.println(CYAN + "\n────────────────────────────────────────────────────" + RESET);
            out.println(YELLOW + "0 - Voltar" + RESET);
            out.print(YELLOW + "→ Escolha: " + RESET);

            String opcao = sc.nextLine().trim();  // Lê a escolha do usuário

            // Caso o usuário queira voltar, encerramos o menu
            if (opcao.equals("0")) {
                logger.info("Voltando ao menu principal a partir do MenuListarCarros.");
                return;
            }

            // Entrada inválida: mostra mensagem de erro e continua no loop
            out.println(RED + BOLD + "\n✖ Opcão inválida! Tente novamente." + RESET);
            out.println(CYAN + "────────────────────────────────────────────────────\n" + RESET);
        }
    }
}
