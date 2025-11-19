package com.example.app.menu;

import com.example.model.Utilizador;
import com.example.service.UserService;
import com.example.utils.FileUtils;
import com.example.utils.PasswordUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class MenuGestaoUtilizadores {

    private static final Logger logger = LogManager.getLogger(MenuGestaoUtilizadores.class);

    private final UserService userService;
    private final Scanner sc = new Scanner(System.in);

    public MenuGestaoUtilizadores(UserService userService) {
        this.userService = userService;
    }

    public void mostrar() {

        int opcao;

        do {
            System.out.println("\n==== Gestão de Utilizadores ====");
            System.out.println("1 - Listar / Procurar utilizadores");
            System.out.println("2 - Criar utilizador");
            System.out.println("3 - Alterar utilizador");
            System.out.println("4 - Remover utilizador");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> menuListagem();
                case 2 -> criar();
                case 3 -> alterar();
                case 4 -> remover();
                case 0 -> logger.info("Voltando ao menu anterior.");
                default -> logger.warn("Opção inválida no menu principal: {}", opcao);
            }

        } while (opcao != 0);
    }

    // --------------------
    // SUB-MENU DE LISTAGEM
    // --------------------
    private void menuListagem() {
        int opcao;

        do {
            System.out.println("\n==== Listagem de Utilizadores ====");
            System.out.println("1 - Listar todos (ordenado)");
            System.out.println("2 - Filtrar por tipo");
            System.out.println("3 - Procurar por nome");
            System.out.println("4 - Listar com paginação");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao) {
                case 1 -> listarOrdenado();
                case 2 -> filtrarTipo();
                case 3 -> procurarNome();
                case 4 -> listarPaginado();
                case 0 -> logger.info("Voltando ao menu de gestão de utilizadores.");
                default -> logger.warn("Opção inválida no sub-menu de listagem: {}", opcao);
            }

        } while (opcao != 0);
    }

    private void listarOrdenado() {
        List<Utilizador> lista = userService.listarOrdenado();
        mostrarLista(lista);
        logger.info("Listagem de utilizadores ordenada exibida. Total: {}", lista.size());
    }

    private void filtrarTipo() {
        System.out.print("Tipo (operador/gerente): ");
        String tipo = sc.nextLine();
        List<Utilizador> lista = userService.listarPorTipo(tipo);
        mostrarLista(lista);
        logger.info("Listagem filtrada por tipo '{}' exibida. Total: {}", tipo, lista.size());
    }

    private void procurarNome() {
        System.out.print("Nome a procurar: ");
        String nome = sc.nextLine();
        List<Utilizador> lista = userService.buscarPorNome(nome);
        mostrarLista(lista);
        logger.info("Pesquisa por nome '{}' realizada. Resultados: {}", nome, lista.size());
    }

    private void listarPaginado() {
        System.out.print("Número da página (0 = primeira): ");
        int pg = Integer.parseInt(sc.nextLine());

        System.out.print("Tamanho da página: ");
        int tamanho = Integer.parseInt(sc.nextLine());

        List<Utilizador> lista = userService.listarPaginado(pg, tamanho);
        mostrarLista(lista);
        logger.info("Listagem paginada exibida. Página: {}, Tamanho: {}, Registos exibidos: {}", pg, tamanho, lista.size());
    }

    private void mostrarLista(List<Utilizador> lista) {
        System.out.println("\n===== Lista =====");

        if (lista.isEmpty()) {
            System.out.println("Nenhum utilizador encontrado.");
            logger.info("Lista vazia exibida.");
            return;
        }

        int index = 1;
        for (Utilizador u : lista) {
            System.out.println(index++ + ". " + u.getUsername() +
                    " | " + u.getNome() + " " + u.getSobrenome() +
                    " | " + u.getTipo());
        }
    }

    // -------------------
    // CRIAR / ALTERAR / REMOVER
    // -------------------
private void criar() {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    String dataNascimento;

    System.out.print("Novo username: ");
    String username = sc.nextLine();

    System.out.print("Password: ");
    String pass = sc.nextLine();
    String hash = PasswordUtils.hash(pass);
    logger.debug("Password digitada: {}, Hash gerado: {}", pass, hash);

    System.out.print("Nome: ");
    String nome = sc.nextLine();

    System.out.print("Sobrenome: ");
    String sobrenome = sc.nextLine();

    System.out.print("Descrição: ");
    String descricao = sc.nextLine();

    // --- Nacionalidade com validação ---
    List<String> nacionalidadesValidas = FileUtils.carregarNacionalidades();
    String nacionalidade;
    while (true) {
        System.out.print("Nacionalidade: ");
        nacionalidade = sc.nextLine();
        if (nacionalidadesValidas.contains(nacionalidade)) {
            break;
        } else {
            System.out.println("Nacionalidade inválida. Valores válidos: " + nacionalidadesValidas);
            logger.warn("Nacionalidade inválida digitada: {}", nacionalidade);
        }
    }

    // --- Data de nascimento com validação ---
    while (true) {
        System.out.print("Data de nascimento (AAAA-MM-DD): ");
        dataNascimento = sc.nextLine();
        try {
            LocalDate.parse(dataNascimento, formatter);
            break; // formato válido
        } catch (DateTimeParseException e) {
            System.out.println("Formato inválido. Use AAAA-MM-DD.");
            logger.warn("Data de nascimento inválida digitada: {}", dataNascimento);
        }
    }

    System.out.print("Salário: ");
    double salario = Double.parseDouble(sc.nextLine());

    System.out.print("Turno entrada (HH:mm): ");
    String horaEntrada = sc.nextLine();

    System.out.print("Turno saída (HH:mm): ");
    String horaSaida = sc.nextLine();

    String tipo;
    do {
        System.out.print("Tipo (operador/gerente): ");
        tipo = sc.nextLine().toLowerCase();
    } while (!tipo.equals("operador") && !tipo.equals("gerente"));

    Utilizador novo = new Utilizador(
            username, hash, tipo, nome, sobrenome,
            descricao, nacionalidade, dataNascimento,
            salario, horaEntrada, horaSaida
    );

    userService.addUser(novo);
    logger.info("Novo utilizador criado: {} ({})", username, tipo);
}

private void alterar() {
    listarOrdenado();

    System.out.print("Username a alterar: ");
    String uname = sc.nextLine();

    Utilizador atual = userService.getByUsername(uname);

    if (atual == null) {
        System.out.println("Utilizador não encontrado.");
        logger.warn("Tentativa de alterar utilizador inexistente: {}", uname);
        return;
    }

    System.out.println("Deixar vazio para manter o valor atual.\n");

    System.out.print("Novo nome (" + atual.getNome() + "): ");
    String nome = sc.nextLine();
    if (!nome.isEmpty()) atual.setNome(nome);

    System.out.print("Novo sobrenome (" + atual.getSobrenome() + "): ");
    String sb = sc.nextLine();
    if (!sb.isEmpty()) atual.setSobrenome(sb);

    System.out.print("Nova descrição (" + atual.getDescricao() + "): ");
    String desc = sc.nextLine();
    if (!desc.isEmpty()) atual.setDescricao(desc);

    // --- Nacionalidade com validação ---
    List<String> nacionalidadesValidas = FileUtils.carregarNacionalidades();
    System.out.print("Nova nacionalidade (" + atual.getNacionalidade() + "): ");
    String nac = sc.nextLine();
    if (!nac.isEmpty()) {
        while (!nacionalidadesValidas.contains(nac)) {
            System.out.println("Nacionalidade inválida. Valores válidos: " + nacionalidadesValidas);
            logger.warn("Nacionalidade inválida digitada: {}", nac);
            System.out.print("Nova nacionalidade (" + atual.getNacionalidade() + "): ");
            nac = sc.nextLine();
        }
        atual.setNacionalidade(nac);
    }

    // --- Data de nascimento com validação ---
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    System.out.print("Nova data (" + atual.getDataNascimento() + "): ");
    String dataInput = sc.nextLine();
    if (!dataInput.isEmpty()) {
        boolean valido = false;
        while (!valido) {
            try {
                LocalDate.parse(dataInput, formatter);
                atual.setDataNascimento(dataInput);
                valido = true;
            } catch (DateTimeParseException e) {
                System.out.println("Formato inválido. Use AAAA-MM-DD.");
                logger.warn("Data de nascimento inválida digitada: {}", dataInput);
                System.out.print("Nova data (" + atual.getDataNascimento() + "): ");
                dataInput = sc.nextLine();
            }
        }
    }

    System.out.print("Novo salário (" + atual.getSalario() + "): ");
    String sal = sc.nextLine();
    if (!sal.isEmpty()) atual.setSalario(Double.parseDouble(sal));

    System.out.print("Novo turno entrada (" + atual.getTurnoEntrada() + "): ");
    String te = sc.nextLine();
    if (!te.isEmpty()) atual.setTurnoEntrada(te);

    System.out.print("Novo turno saída (" + atual.getTurnoSaida() + "): ");
    String ts = sc.nextLine();
    if (!ts.isEmpty()) atual.setTurnoSaida(ts);

    System.out.print("Novo tipo (" + atual.getTipo() + "): ");
    String tipo = sc.nextLine();
    if (!tipo.isEmpty()) atual.setTipo(tipo);

    userService.updateUser(uname, atual);
    System.out.println("Utilizador atualizado.");
    logger.info("Utilizador atualizado: {}", uname);
}


    private void remover() {
        listarOrdenado();

        System.out.print("Username a remover: ");
        String uname = sc.nextLine();

        userService.removeUser(uname);
        System.out.println("Removido com sucesso.");
        logger.info("Utilizador removido: {}", uname);
    }
}
