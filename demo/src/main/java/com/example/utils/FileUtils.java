package com.example.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class FileUtils {

    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    private static final String DATA_FOLDER = "SistemaOrganizacaoData";
    private static File folder;
    private static File usersFile;
    private static File turnosFile;
    private static File nacionalidadesFile;

    // Lista padrão de nacionalidades
    private static final List<String> NACIONALIDADES_PADRAO = Arrays.asList(
            "Portugal", "Espanha", "Franca", "Alemanha", "Brasil", "Italia", "Reino Unido"
            // adiciona mais se necessário
    );

    public static void initialize() {
        folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
            logger.info("Pasta de dados criada: {}", folder.getAbsolutePath());
        }

        // Users file
        usersFile = new File(folder, "users.json");
        if (!usersFile.exists()) {
            try {
                usersFile.createNewFile();
                logger.info("Ficheiro users.json criado em: {}", usersFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro users.json", e);
            }
        }

        // Turnos file
        turnosFile = new File(folder, "turnos.txt");
        if (!turnosFile.exists()) {
            try {
                turnosFile.createNewFile();
                logger.info("Ficheiro turnos.txt criado em: {}", turnosFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro turnos.txt", e);
            }
        }

        // Nacionalidades file
        nacionalidadesFile = new File(folder, "nacionalidades.json");
        if (!nacionalidadesFile.exists()) {
            try {
                nacionalidadesFile.createNewFile();
                logger.info("Ficheiro nacionalidades.json criado em: {}", nacionalidadesFile.getAbsolutePath());

                // Preencher com nacionalidades padrão
                salvarNacionalidades(NACIONALIDADES_PADRAO);
                logger.info("Nacionalidades padrão gravadas em nacionalidades.json");
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro nacionalidades.json", e);
            }
        }
    }

    public static void salvarNacionalidades(List<String> nacionalidades) {
        try (Writer writer = new FileWriter(nacionalidadesFile)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(nacionalidades, writer);
        } catch (IOException e) {
            logger.error("Erro ao gravar nacionalidades em {}", nacionalidadesFile.getAbsolutePath(), e);
        }
    }

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

    public static File getUsersFile() {
        return usersFile;
    }

    public static File getTurnosFile() {
        return turnosFile;
    }

    public static File getNacionalidadesFile() {
        return nacionalidadesFile;
    }

    public static File getDataFolder() {
        return folder;
    }
}

