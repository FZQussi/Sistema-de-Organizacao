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

    public MenuAlterarUtilizador(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {
        ConsoleUtils.clear();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        System.out.println("\n=== Alterar Utilizador ===");
        System.out.println("Digite 'ESC' a qualquer momento para cancelar e voltar ao menu anterior.\n");

        System.out.print("Username do utilizador a alterar: ");
        String uname = sc.nextLine();
        if (uname.equalsIgnoreCase("ESC")) {
            System.out.println("Operação cancelada. Voltando ao menu anterior.");
            return;
        }

        Utilizador atual = userService.getByUsername(uname);
        if (atual == null) {
            System.out.println("❌ Utilizador não encontrado.");
            logger.warn("Tentativa de alterar utilizador inexistente: {}", uname);
            return;
        }

        System.out.println("\n(Deixe em branco para manter o valor atual, ou digite 'ESC' para cancelar)");

        // =========================
        // Campos para alteração
        // =========================
        System.out.print("Nome (" + atual.getNome() + "): ");
        String nome = sc.nextLine();
        if (nome.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!nome.isEmpty()) atual.setNome(nome);

        System.out.print("Sobrenome (" + atual.getSobrenome() + "): ");
        String sobrenome = sc.nextLine();
        if (sobrenome.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!sobrenome.isEmpty()) atual.setSobrenome(sobrenome);

        System.out.print("Descrição (" + atual.getDescricao() + "): ");
        String descricao = sc.nextLine();
        if (descricao.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!descricao.isEmpty()) atual.setDescricao(descricao);

        // -------- Nacionalidade validada --------
        List<String> nacionalidadesValidas = FileUtils.carregarNacionalidades();
        System.out.print("Nacionalidade (" + atual.getNacionalidade() + "): ");
        String nac = sc.nextLine();
        if (nac.equalsIgnoreCase("ESC")) { cancel(); return; }

        if (!nac.isEmpty()) {
            while (!nacionalidadesValidas.contains(nac)) {
                System.out.println("❌ Nacionalidade inválida.");
                System.out.println("Valores válidos: " + nacionalidadesValidas);
                logger.warn("Nacionalidade inválida digitada: {}", nac);

                System.out.print("Nacionalidade (" + atual.getNacionalidade() + "): ");
                nac = sc.nextLine();
                if (nac.equalsIgnoreCase("ESC")) { cancel(); return; }
            }
            atual.setNacionalidade(nac);
        }

        // -------- Data validada --------
        System.out.print("Data de nascimento (" + atual.getDataNascimento() + "): ");
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
                    System.out.println("❌ Formato inválido. Use AAAA-MM-DD.");
                    logger.warn("Data inválida digitada: {}", dataInput);
                    System.out.print("Data de nascimento (" + atual.getDataNascimento() + "): ");
                    dataInput = sc.nextLine();
                    if (dataInput.equalsIgnoreCase("ESC")) { cancel(); return; }
                }
            }
        }

        // -------- Salário --------
        System.out.print("Salário (" + atual.getSalario() + "): ");
        String sal = sc.nextLine();
        if (sal.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!sal.isEmpty()) atual.setSalario(Double.parseDouble(sal));

        // -------- Turno entrada --------
        System.out.print("Hora Entrada (" + atual.getTurnoEntrada() + "): ");
        String te = sc.nextLine();
        if (te.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!te.isEmpty()) atual.setTurnoEntrada(te);

        // -------- Turno saída --------
        System.out.print("Hora Saída (" + atual.getTurnoSaida() + "): ");
        String ts = sc.nextLine();
        if (ts.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!ts.isEmpty()) atual.setTurnoSaida(ts);

        // -------- Tipo --------
        System.out.print("Tipo (" + atual.getTipo() + ") [operador/gerente]: ");
        String tipo = sc.nextLine();
        if (tipo.equalsIgnoreCase("ESC")) { cancel(); return; }
        if (!tipo.isEmpty()) atual.setTipo(tipo);

        // Guardar alterações
        userService.updateUser(uname, atual);

        System.out.println("\n✔ Utilizador atualizado com sucesso!");
        logger.info("Utilizador alterado: {}", uname);
    }

    private void cancel() {
        System.out.println("\n⚠ Operação cancelada. Voltando ao menu anterior.");
        logger.info("Operação de alteração de utilizador cancelada pelo utilizador.");
    }
}
