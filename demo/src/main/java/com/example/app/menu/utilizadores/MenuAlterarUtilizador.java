package com.example.app.menu.utilizadores;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.ConsoleUtils;
import com.example.utils.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuAlterarUtilizador {

    private static final Logger logger = LogManager.getLogger(MenuAlterarUtilizador.class);

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    // Cores ANSI
    private static final String RESET = "\u001B[0m";
    private static final String CYAN = "\u001B[36m";
    private static final String GREEN = "\u001B[32m";
    private static final String RED = "\u001B[31m";
    private static final String YELLOW = "\u001B[33m";
    private static final String BOLD = "\u001B[1m";

    public MenuAlterarUtilizador(UserService userService) {
        this.userService = userService;
    }

    private void header() {
        System.out.println(CYAN + BOLD + "╔══════════════════════════════════════════════╗");
        System.out.println("║              ALTERAR UTILIZADOR              ║");
        System.out.println("╚══════════════════════════════════════════════╝" + RESET);
        System.out.println();
    }

    public void mostrar() {
        ConsoleUtils.clear();
        header();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println(YELLOW + "Digite 'ESC' para cancelar a qualquer momento.\n" + RESET);

        System.out.print(YELLOW + "→ Username do utilizador a alterar: " + RESET);
        String uname = sc.nextLine();
        if (uname.equalsIgnoreCase("ESC")) { cancel(); return; }

        Utilizador atual = userService.getByUsername(uname);
        if (atual == null) {
            System.out.println(RED + BOLD + "✖ Utilizador não encontrado.\n" + RESET);
            logger.warn("Tentativa de alterar utilizador inexistente: {}", uname);
            return;
        }

        System.out.println(CYAN + "\n──────────────────────────────────────────────" + RESET);
        System.out.println(YELLOW + "(Deixe em branco para manter o valor atual)\n" + RESET);

        // ========== Nome ==========
        System.out.print(YELLOW + "→ Nome (" + atual.getNome() + "): " + RESET);
        String nome = sc.nextLine();
        if (nome.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!nome.isEmpty()) atual.setNome(nome);

        // ========== Sobrenome ==========
        System.out.print(YELLOW + "→ Sobrenome (" + atual.getSobrenome() + "): " + RESET);
        String sobrenome = sc.nextLine();
        if (sobrenome.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!sobrenome.isEmpty()) atual.setSobrenome(sobrenome);

        // ========== Descrição ==========
        System.out.print(YELLOW + "→ Descricão (" + atual.getDescricao() + "): " + RESET);
        String descricao = sc.nextLine();
        if (descricao.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!descricao.isEmpty()) atual.setDescricao(descricao);

        // ========== Nacionalidade (validada) ==========
        List<String> nacionalidadesValidas = FileUtils.carregarNacionalidades();
        
        System.out.print(YELLOW + "→ Nacionalidade (" + atual.getNacionalidade() + "): " + RESET);
        String nac = sc.nextLine();
        if (nac.equalsIgnoreCase("ESC")) { cancel(); return; }

        if (!nac.isEmpty()) {
            while (!nacionalidadesValidas.contains(nac)) {
                System.out.println(RED + "✖ Nacionalidade inválida!" + RESET);
                System.out.println(YELLOW + "Valores válidos: " + nacionalidadesValidas + RESET);
                logger.warn("Nacionalidade inválida digitada: {}", nac);

                System.out.print(YELLOW + "→ Nacionalidade (" + atual.getNacionalidade() + "): " + RESET);
                nac = sc.nextLine();
                if (nac.equalsIgnoreCase("ESC")) { cancel(); return; }
            }
            atual.setNacionalidade(nac);
        }

        // ========== Data de nascimento ==========
        System.out.print(YELLOW + "→ Data de nascimento (" + atual.getDataNascimento() + "): " + RESET);
        String dataInput = sc.nextLine();
        if (dataInput.equalsIgnoreCase("ESC")) { cancel(); return; }

        if (!dataInput.isEmpty()) {
            boolean valido = false;
            while (!valido) {
                try {
                    LocalDate.parse(dataInput, formatter);
                    atual.setDataNascimento(dataInput);
                    valido = true;
                } catch (DateTimeParseException e) {
                    System.out.println(RED + "✖ Formato inválido! Use AAAA-MM-DD." + RESET);
                    logger.warn("Data inválida digitada: {}", dataInput);

                    System.out.print(YELLOW + "→ Data de nascimento (" + atual.getDataNascimento() + "): " + RESET);
                    dataInput = sc.nextLine();
                    if (dataInput.equalsIgnoreCase("ESC")) { cancel(); return; }
                }
            }
        }

        // ========== Salário ==========
        System.out.print(YELLOW + "→ Salário (" + atual.getSalario() + "): " + RESET);
        String sal = sc.nextLine();
        if (sal.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!sal.isEmpty()) atual.setSalario(Double.parseDouble(sal));

        // ========== Turno Entrada ==========
        System.out.print(YELLOW + "→ Hora Entrada (" + atual.getTurnoEntrada() + "): " + RESET);
        String te = sc.nextLine();
        if (te.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!te.isEmpty()) atual.setTurnoEntrada(te);

        // ========== Turno Saída ==========
        System.out.print(YELLOW + "→ Hora Saída (" + atual.getTurnoSaida() + "): " + RESET);
        String ts = sc.nextLine();
        if (ts.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!ts.isEmpty()) atual.setTurnoSaida(ts);

        // ========== Tipo ==========
        System.out.print(YELLOW + "→ Tipo (" + atual.getTipo() + ") [operador/gerente]: " + RESET);
        String tipo = sc.nextLine();
        if (tipo.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!tipo.isEmpty()) atual.setTipo(tipo);

        // ========== Guardar ==========
        userService.updateUser(uname, atual);

        System.out.println(CYAN + "\n──────────────────────────────────────────────" + RESET);
        System.out.println(GREEN + BOLD + "✔ Utilizador atualizado com sucesso!" + RESET);
        logger.info("Utilizador alterado: {}", uname);
    }

    private void cancel() {
        System.out.println(RED + "\n⚠ Operacão cancelada. Voltando ao menu anterior." + RESET);
        logger.info("Operacão de alteracão cancelada pelo utilizador.");
    }
    // MÉTODO AUXILIAR PARA TESTES (sem Scanner)
public boolean alterarUtilizador(
        String username,
        String nome,
        String sobrenome,
        String descricao,
        String nacionalidade,
        String dataNascimento,
        String salario,
        String turnoEntrada,
        String turnoSaida,
        String tipo) {

    Utilizador atual = userService.getByUsername(username);
    if (atual == null) return false;

    if (!nome.isEmpty()) atual.setNome(nome);
    if (!sobrenome.isEmpty()) atual.setSobrenome(sobrenome);
    if (!descricao.isEmpty()) atual.setDescricao(descricao);
    if (!nacionalidade.isEmpty()) atual.setNacionalidade(nacionalidade);
    if (!dataNascimento.isEmpty()) atual.setDataNascimento(dataNascimento);
    if (!salario.isEmpty()) atual.setSalario(Double.parseDouble(salario));
    if (!turnoEntrada.isEmpty()) atual.setTurnoEntrada(turnoEntrada);
    if (!turnoSaida.isEmpty()) atual.setTurnoSaida(turnoSaida);
    if (!tipo.isEmpty()) atual.setTipo(tipo);

    userService.updateUser(username, atual);

    return true;
}

}

