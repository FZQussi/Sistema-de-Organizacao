package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;

/**
 * Classe utilitária para gestão de ficheiros e pastas do sistema.
 * Responsável por criar a estrutura de dados inicial e fornecer
 * acesso aos ficheiros de utilizadores, turnos, nacionalidades,
 * movimentos e pagamentos.
 */
public class FileUtils {

    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    private static final String DATA_FOLDER = "SistemaOrganizacaoData";

    // Referências para os ficheiros e pasta de dados
    private static File folder;
    private static File usersFile;
    private static File turnosFile;
    private static File nacionalidadesFile;
    private static File pagamentosFile;   // ficheiro de pagamentos
    private static File movimentosFile;   // ficheiro de movimentos

    // Lista padrão de nacionalidades
    private static final List<String> NACIONALIDADES_PADRAO = Arrays.asList(
            "Portugal", "Espanha", "Franca", "Alemanha", "Brasil", "Italia", "Reino Unido"
    );

    /**
     * Inicializa a estrutura de pastas e ficheiros do sistema.
     * Se não existirem, cria-os e preenche ficheiros padrão.
     */
    public static void initialize() {
        // Cria a pasta principal se não existir
        folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
            logger.info("Pasta de dados criada: {}", folder.getAbsolutePath());
        }

        // --------------------- Movimentos ---------------------
        movimentosFile = new File(folder, "movimentos.txt");
        if (!movimentosFile.exists()) {
            try {
                movimentosFile.createNewFile();
                logger.info("Ficheiro movimentos.txt criado em: {}", movimentosFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro movimentos.txt", e);
            }
        }

        // --------------------- Users --------------------------
        usersFile = new File(folder, "users.json");
        if (!usersFile.exists()) {
            try {
                usersFile.createNewFile();
                logger.info("Ficheiro users.json criado em: {}", usersFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro users.json", e);
            }
        }

        // --------------------- Turnos -------------------------
        turnosFile = new File(folder, "turnos.txt");
        if (!turnosFile.exists()) {
            try {
                turnosFile.createNewFile();
                logger.info("Ficheiro turnos.txt criado em: {}", turnosFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro turnos.txt", e);
            }
        }

        // --------------------- Nacionalidades ----------------
        nacionalidadesFile = new File(folder, "nacionalidades.json");
        if (!nacionalidadesFile.exists()) {
            try {
                nacionalidadesFile.createNewFile();
                logger.info("Ficheiro nacionalidades.json criado em: {}", nacionalidadesFile.getAbsolutePath());

                // Grava nacionalidades padrão
                salvarNacionalidades(NACIONALIDADES_PADRAO);
                logger.info("Nacionalidades padrão gravadas em nacionalidades.json");
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro nacionalidades.json", e);
            }
        }

        // --------------------- Pagamentos --------------------
        pagamentosFile = new File(folder, "pagamentos.txt");
        if (!pagamentosFile.exists()) {
            try {
                pagamentosFile.createNewFile();
                logger.info("Ficheiro pagamentos.txt criado em: {}", pagamentosFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro pagamentos.txt", e);
            }
        }
    }

    /**
     * Grava a lista de nacionalidades no ficheiro nacionalidades.json
     */
    public static void salvarNacionalidades(List<String> nacionalidades) {
        try (Writer writer = new FileWriter(nacionalidadesFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(nacionalidades, writer);
        } catch (IOException e) {
            logger.error("Erro ao gravar nacionalidades em {}", nacionalidadesFile.getAbsolutePath(), e);
        }
    }

    /**
     * Carrega a lista de nacionalidades do ficheiro.
     * Se ocorrer algum erro, retorna a lista padrão.
     */
    public static List<String> carregarNacionalidades() {
        try (Reader reader = new FileReader(nacionalidadesFile)) {
            Gson gson = new Gson();
            String[] array = gson.fromJson(reader, String[].class);
            return array != null ? Arrays.asList(array) : NACIONALIDADES_PADRAO;
        } catch (IOException e) {
            logger.error("Erro ao ler nacionalidades de {}", nacionalidadesFile.getAbsolutePath(), e);
            return NACIONALIDADES_PADRAO;
        }
    }

    // ------------------- GETTERS ----------------------------

    public static File getUsersFile() {
        return usersFile;
    }

    public static File getTurnosFile() {
        return turnosFile;
    }

    public static File getNacionalidadesFile() {
        return nacionalidadesFile;
    }

    public static File getPagamentosFile() {
        return pagamentosFile;
    }

    public static File getMovimentosFile() {
        return movimentosFile;
    }

    public static File getDataFolder() {
        return folder;
    }
}
