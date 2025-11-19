package com.example.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    private static final Logger logger = LogManager.getLogger(FileUtils.class);

    private static final String DATA_FOLDER = "SistemaOrganizacaoData";
    private static File folder;
    private static File usersFile;
    private static File turnosFile;

    public static void initialize() {
        folder = new File(DATA_FOLDER);
        if (!folder.exists()) {
            folder.mkdir();
            logger.info("Pasta de dados criada: {}", folder.getAbsolutePath());
        }

        usersFile = new File(folder, "users.json");
        if (!usersFile.exists()) {
            try {
                usersFile.createNewFile();
                logger.info("Ficheiro users.json criado em: {}", usersFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro users.json", e);
            }
        }

        turnosFile = new File(folder, "turnos.txt");
        if (!turnosFile.exists()) {
            try {
                turnosFile.createNewFile();
                logger.info("Ficheiro turnos.txt criado em: {}", turnosFile.getAbsolutePath());
            } catch (IOException e) {
                logger.error("Erro ao criar ficheiro turnos.txt", e);
            }
        }
    }

    public static File getUsersFile() {
        return usersFile;
    }

    public static File getTurnosFile() {
        return turnosFile;
    }

    public static File getDataFolder() {
        return folder;
    }
}
